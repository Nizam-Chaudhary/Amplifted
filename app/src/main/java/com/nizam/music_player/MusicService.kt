package com.nizam.music_player

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "Amplifted")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }


    fun showNotification(playPauseButton: Int) {

        val previousIntent = Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val previousPendingIntent = PendingIntent.getBroadcast(baseContext,0,previousIntent,PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext,1,nextIntent,PendingIntent.FLAG_IMMUTABLE)

        val playPauseIntent = Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PLAY_PAUSE)
        val playPausePendingIntent = PendingIntent.getBroadcast(baseContext,2,playPauseIntent,PendingIntent.FLAG_IMMUTABLE)

        val imageArt = getImageArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)

        //creating Bitmap from ByteArray
       val image = if ( imageArt != null) {
            BitmapFactory.decodeByteArray(imageArt,0,imageArt.size)
        } else {
            BitmapFactory.decodeResource(this@MusicService.resources,R.drawable.music_icon_notification)
        }
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .addAction(R.drawable.previous_icon_notification, "Previous", previousPendingIntent)
            .addAction(playPauseButton, "Play", playPausePendingIntent)
            .addAction(R.drawable.next_icon_notification, "Next", nextPendingIntent)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setSmallIcon(R.drawable.music_icon_notification)
            .setLargeIcon(image)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setGroup(ApplicationClass.CHANNEL_ID)
            .build()
        startForeground(7, notification)
    }
    fun createMediaPlayer() {
        try{
            if(PlayerActivity.musicService!!.mediaPlayer == null) {
                PlayerActivity.musicService!!.mediaPlayer = MediaPlayer()
                createMediaPlayer()
            } else {
                PlayerActivity.musicService!!.mediaPlayer!!.reset()
                PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
                PlayerActivity.musicService!!.mediaPlayer!!.prepare()
                setLayout(baseContext)
                PlayerActivity.musicService!!.mediaPlayer!!.start()
                PlayerActivity.isSongPlaying = true
                PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
                showNotification(R.drawable.pause_icon_notification)
            }
        } catch(_:Exception) {}
    }

    fun syncSeekBar() {
        runnable = Runnable {
            PlayerActivity.binding.progressDuration.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,200)
    }
}