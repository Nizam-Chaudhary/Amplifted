package com.nizam.music_player

import android.content.Context
import android.media.MediaMetadataRetriever
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.concurrent.TimeUnit
import kotlin.random.Random


//global function to format duration of songs.
fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - minutes * TimeUnit.SECONDS.convert(1,
        TimeUnit.MINUTES))
    return String.format("%02d:%02d",minutes,seconds)
}

//this function is used to retrieve ByteArray of our album art.
fun getImageArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

fun playPreviousSong() {
    if(PlayerActivity.lastSong != -1) {
        PlayerActivity.songPosition = PlayerActivity.lastSong
        PlayerActivity.lastSong = -1
    } else {
        if(PlayerActivity.songPosition == 0) {
            PlayerActivity.songPosition = PlayerActivity.musicListPA.size - 1
        } else {
            PlayerActivity.songPosition--
        }
    }
}

//this function is used to play the next song and it responds.
fun playNextSong() {
    if(PlayerActivity.shuffle){
        PlayerActivity.lastSong = PlayerActivity.songPosition
        PlayerActivity.songPosition = getRandomNumber()
    } else {
        if(PlayerActivity.songPosition == PlayerActivity.musicListPA.size - 1 ) {
            PlayerActivity.songPosition = 0
        } else {
            PlayerActivity.songPosition++
        }
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
    if(PlayerActivity.repeat) {
        PlayerActivity.binding.repeatSong.setImageResource(R.drawable.repeat_icon_true)
    } else {
        PlayerActivity.binding.repeatSong.setImageResource(R.drawable.repeat_icon)
    }
}

//generates Random number other than current song playing.
fun getRandomNumber():Int {
    var temp = Random.nextInt(0, PlayerActivity.musicListPA.size)
    while (temp == PlayerActivity.songPosition) {
        temp = Random.nextInt(
            0,
            PlayerActivity.musicListPA.size
        )
        if(temp != PlayerActivity.songPosition) {
            break
        }
    }
    return temp
}