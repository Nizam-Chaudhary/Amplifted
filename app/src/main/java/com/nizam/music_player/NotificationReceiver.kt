package com.nizam.music_player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        when(p1?.action) {
            ApplicationClass.PREVIOUS -> {
                playPreviousSong()
                PlayerActivity.musicService!!.createMediaPlayer(false)
            }
            ApplicationClass.NEXT -> {
                playNextSong()
                PlayerActivity.musicService!!.createMediaPlayer(false)

            }
            ApplicationClass.PLAY_PAUSE -> if(PlayerActivity.isSongPlaying) pauseMusic() else playMusic()
        }
    }


    //plays the music from notification
    private fun playMusic() {
        PlayerActivity.isSongPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon_notification,PlaybackStateCompat.STATE_PLAYING)
        NowPlaying.binding.nowPlayingPlayPause.setImageResource(R.drawable.pause_icon_notification)
        PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
    }

    //pauses the music from notification
    private fun pauseMusic() {
        PlayerActivity.isSongPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_icon_notification,PlaybackStateCompat.STATE_PAUSED)
        NowPlaying.binding.nowPlayingPlayPause.setImageResource(R.drawable.play_icon_notification)
        PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.play_icon)
    }

}