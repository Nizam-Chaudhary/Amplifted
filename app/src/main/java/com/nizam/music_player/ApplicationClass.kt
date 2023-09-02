package com.nizam.music_player

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class ApplicationClass: Application() {
    companion object{
        const val CHANNEL_ID= "Channel 1"
        const val PLAY_PAUSE= "PlayPause"
        const val NEXT= "Next"
        const val PREVIOUS= "Previous"
    }

    override fun onCreate() {
        setTheme()
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,"Now Playing", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = "It is important channel to play songs."
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun setTheme() {
        when (SharedPreferencesAmplifted(this).getUiMode()) {
            "light" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            "dark" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            "system" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }
}