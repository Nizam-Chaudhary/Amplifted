package com.nizam.music_player

import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

class PlayerMediaSessionCallback(private val mediaPlayer: MediaPlayer) : MediaSessionCompat.Callback() {
    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
        mediaPlayer.seekTo(pos.toInt())
    }

    override fun onPause() {
        super.onPause()
        pauseMusic()
    }

    override fun onPlay() {
        super.onPlay()
        playMusic()
    }

    override fun onSkipToNext() {
        playNextSong()
        PlayerActivity.musicService!!.createMediaPlayer()
    }

    override fun onSkipToPrevious() {
        playPreviousSong()
        PlayerActivity.musicService!!.createMediaPlayer()
    }

    private fun playMusic() {
        PlayerActivity.isSongPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon_notification,
            PlaybackStateCompat.STATE_PLAYING)
        NowPlaying.binding.nowPlayingPlayPause.setImageResource(R.drawable.pause_icon_notification)
        PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
    }

    private fun pauseMusic() {
        PlayerActivity.isSongPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_icon_notification,PlaybackStateCompat.STATE_PAUSED)
        NowPlaying.binding.nowPlayingPlayPause.setImageResource(R.drawable.play_icon_notification)
        PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.play_icon)
    }
}