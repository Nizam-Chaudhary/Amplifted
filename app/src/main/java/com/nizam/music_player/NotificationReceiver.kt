package com.nizam.music_player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlin.system.exitProcess

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        when(p1?.action) {
            ApplicationClass.PREVIOUS -> Toast.makeText(p0,"Previous",Toast.LENGTH_SHORT).show()
            ApplicationClass.NEXT -> Toast.makeText(p0,"Next",Toast.LENGTH_SHORT).show()
            ApplicationClass.PLAY_PAUSE -> if(PlayerActivity.isSongPlaying) pauseMusic() else playMusic()
            ApplicationClass.EXIT -> {
                PlayerActivity.musicService!!.stopForeground(true)
                PlayerActivity.musicService = null
                exitProcess(1)
            }
        }
    }
    private fun playMusic() {
        PlayerActivity.isSongPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon_notification)
        PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
    }

    private fun pauseMusic() {
        PlayerActivity.isSongPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_icon_notification)
        PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.play_icon)
    }

}