package com.nizam.music_player

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
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - minutes * 60)
    return String.format("%02d:%02d",minutes,seconds)
}