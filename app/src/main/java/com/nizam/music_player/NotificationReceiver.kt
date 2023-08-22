package com.nizam.music_player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        when(p1?.action) {
            ApplicationClass.PREVIOUS -> {
                playPreviousSong()
                PlayerActivity.musicService!!.createMediaPlayer()
            }
            ApplicationClass.NEXT -> {
                playNextSong()
                PlayerActivity.musicService!!.createMediaPlayer()

            }
            ApplicationClass.PLAY_PAUSE -> if(PlayerActivity.isSongPlaying) pauseMusic() else playMusic()
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