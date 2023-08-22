package com.nizam.music_player

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass: Application() {
    companion object{
        const val CHANNEL_ID= "Channel 1"
        const val PLAY_PAUSE= "PlayPause"
        const val NEXT= "Next"
        const val PREVIOUS= "Previous"
    }

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,"Now Playing", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = "It is important channel to play songs."
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}