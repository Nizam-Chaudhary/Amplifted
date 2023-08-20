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
) {

}

fun formatDuration(duration: Long):String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS).toInt()
    val second = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - minutes*TimeUnit.SECONDS.convert(1,TimeUnit.SECONDS)).toInt()
    return (String().format("%2d:%2d",minutes,second))
}