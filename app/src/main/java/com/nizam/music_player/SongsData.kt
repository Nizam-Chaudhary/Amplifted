package com.nizam.music_player

import android.net.Uri

data class SongsData(
    val id: Uri,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri: String,
    val dateModified: String?
)
