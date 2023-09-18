package com.nizam.music_player

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nizam.music_player.PlayerActivity.Companion.musicListPA
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

var stopped = false

//global function to format duration of songs.
fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(
        duration,
        TimeUnit.MILLISECONDS
    ) - minutes * TimeUnit.SECONDS.convert(
        1, TimeUnit.MINUTES
    ))
    return String.format("%02d:%02d", minutes, seconds)
}

//this function is used to retrieve ByteArray of our album art.
fun getImageArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

fun playPreviousSong() {
    if (musicListPA.size == 1) {
        return
    }
    if (PlayerActivity.lastSong != -1) {
        PlayerActivity.songPosition = PlayerActivity.lastSong
        PlayerActivity.lastSong = -1
    } else {
        if (PlayerActivity.songPosition == 0) {
            PlayerActivity.songPosition = musicListPA.size - 1
        } else {
            PlayerActivity.songPosition--
        }
    }
}

//this function is used to play the next song and it responds.
fun playNextSong() {
    if (PlayerActivity.shuffle) {
        PlayerActivity.lastSong = PlayerActivity.songPosition
        PlayerActivity.songPosition = getRandomNumber()
    } else {
        if (PlayerActivity.songPosition == musicListPA.size - 1) {
            PlayerActivity.songPosition = 0
        } else {
            PlayerActivity.songPosition++
        }
    }
}

fun setLayout(context: Context, external: Boolean) {
    if (external) {
        Glide.with(context).load(getImageArt(musicListPA[PlayerActivity.songPosition].path))
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(PlayerActivity.binding.albumImage)
        PlayerActivity.binding.songName.text = musicListPA[PlayerActivity.songPosition].title
        if (!PlayerActivity.isSongPlaying) {
            PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.play_icon)
        }
        PlayerActivity.binding.songName.text = formatName()
    } else {
        Glide.with(context).load(musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(PlayerActivity.binding.albumImage)
        PlayerActivity.binding.songName.text = musicListPA[PlayerActivity.songPosition].title
        if (!PlayerActivity.isSongPlaying) {
            PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.play_icon)
        }

        //adding song to recent
        addToRecent(context)

        val favoritesDB = FavoritesDB(context, null)
        //setting favorites icon.
        if (favoritesDB.songExists(musicListPA[PlayerActivity.songPosition].title)) {
            PlayerActivity.binding.favoritesButton.setImageResource(R.drawable.favorite_filled_icon)
        } else {
            PlayerActivity.binding.favoritesButton.setImageResource(R.drawable.favorite_empty_icon)
        }

        //now setting layout for Now Playing Fragment
        setNowPlaying(context)
        Glide.with(context).load(musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(NowPlaying.binding.nowPlayingAlbumArt)
        NowPlaying.binding.nowPlayingSongName.text = musicListPA[PlayerActivity.songPosition].title
        NowPlaying.binding.nowPlayingArtistName.text =
            musicListPA[PlayerActivity.songPosition].artist
        if (PlayerActivity.repeat) {
            PlayerActivity.binding.repeatSong.setImageResource(R.drawable.repeat_icon_true)
        } else {
            PlayerActivity.binding.repeatSong.setImageResource(R.drawable.repeat_icon)
        }
    }
    PlayerActivity.binding.duration.text =
        formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
    PlayerActivity.binding.seekBarPA.max = PlayerActivity.musicService!!.mediaPlayer!!.duration
    PlayerActivity.musicService!!.syncSeekBar()
}

fun formatName(): CharSequence {
    val title = musicListPA[PlayerActivity.songPosition].title
    return title.substringAfterLast('/', title)
}

fun setNowPlaying(context: Context) {
    //now setting layout for Now Playing Fragment
    if(PlayerActivity.external) {
        Glide.with(context)
            .load(getImageArt(musicListPA[PlayerActivity.songPosition].path))
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(NowPlaying.binding.nowPlayingAlbumArt)
        NowPlaying.binding.nowPlayingSongName.text = formatName()
        NowPlaying.binding.nowPlayingArtistName.text = musicListPA[PlayerActivity.songPosition].artist
        NowPlaying.binding.nowPlayingNext.isEnabled = false
    } else {
        NowPlaying.binding.nowPlayingNext.isEnabled = true
        Glide.with(context).load(musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(NowPlaying.binding.nowPlayingAlbumArt)
        NowPlaying.binding.nowPlayingSongName.text = musicListPA[PlayerActivity.songPosition].title
        NowPlaying.binding.nowPlayingArtistName.text = musicListPA[PlayerActivity.songPosition].artist
    }
}

//generates Random number other than current song playing.
fun getRandomNumber(): Int {
    var temp = Random.nextInt(0, musicListPA.size)
    while (temp == PlayerActivity.songPosition) {
        temp = Random.nextInt(
            0, musicListPA.size
        )
        if (temp != PlayerActivity.songPosition) {
            break
        }
    }
    return temp
}

@SuppressLint("Range")
fun getSongData(favoritesDB: FavoritesDB): ArrayList<SongsData> {
    val songsList: ArrayList<SongsData> = ArrayList()
    val cursor = favoritesDB.getFavorites()
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            do {
                val idC = cursor.getString(cursor.getColumnIndex(FavoritesDB.ID_COL))
                val titleC = cursor.getString(cursor.getColumnIndex(FavoritesDB.TITLE_COL))
                val albumC = cursor.getString(cursor.getColumnIndex(FavoritesDB.ALBUM_COL))
                val artistC = cursor.getString(cursor.getColumnIndex(FavoritesDB.ARTIST_COL))
                val durationC = cursor.getLong(cursor.getColumnIndex(FavoritesDB.DURATION_COL))
                val pathC = cursor.getString(cursor.getColumnIndex(FavoritesDB.PATH_COL))
                val artUriC = cursor.getString(cursor.getColumnIndex(FavoritesDB.ART_URI_COL))
                val dateAddedC =
                    cursor.getString(cursor.getColumnIndex(FavoritesDB.DATE_MODIFIED_COL))

                val music = SongsData(
                    id = Uri.parse(idC),
                    title = titleC,
                    album = albumC,
                    artist = artistC,
                    duration = durationC,
                    path = pathC,
                    artUri = artUriC,
                    dateModified = dateAddedC
                )

                val file = File(music.path)
                if (file.exists()) {
                    songsList.add(music)
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
    }
    return songsList
}

private fun addToRecent(context: Context) {
    val time = Date()
    val formatter =
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault())
    val currentTime = formatter.format(time)
    val recentDB = RecentDB(context, null)
    recentDB.addToRecent(musicListPA[PlayerActivity.songPosition], currentTime)
}