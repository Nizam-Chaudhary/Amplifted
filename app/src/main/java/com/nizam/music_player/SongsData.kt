package com.nizam.music_player

import android.content.Context
import android.media.MediaMetadataRetriever
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.concurrent.TimeUnit

data class SongsData(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri: String
)

//global function to format duration of songs.
fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - minutes * TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES))
    return String.format("%02d:%02d",minutes,seconds)
}

//this function is used to retrieve ByteArray of our album art.
fun getImageArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

fun playPreviousSong() {
    if(PlayerActivity.songPosition == 0) {
        PlayerActivity.songPosition = PlayerActivity.musicListPA.size - 1
    } else {
        PlayerActivity.songPosition--
    }
}

//this function is used to play the next song and it responds.
fun playNextSong() {
    if(PlayerActivity.songPosition == PlayerActivity.musicListPA.size - 1 ) {
        PlayerActivity.songPosition = 0
    } else {
        PlayerActivity.songPosition++
    }
}

fun setLayout(context: Context) {
    Glide.with(context)
        .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
        .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
        .into(PlayerActivity.binding.albumImage)
    PlayerActivity.binding.songName.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
    PlayerActivity.binding.duration.text = formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
    PlayerActivity.binding.seekBarPA.max = PlayerActivity.musicService!!.mediaPlayer!!.duration
    PlayerActivity.musicService!!.syncSeekBar()
}