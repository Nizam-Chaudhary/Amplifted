package com.nizam.music_player

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat

class MusicService : Service(), AudioManager.OnAudioFocusChangeListener {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable
    lateinit var audioManager: AudioManager


    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "Amplifted")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }


    fun showNotification(playPauseButton: Int, currentState: Int) {

        lateinit var mainContentIntent: PendingIntent

        if (!PlayerActivity.external) {
            val intent = Intent(baseContext, MainActivity::class.java)
            mainContentIntent =
                PendingIntent.getActivity(baseContext, 3, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        lateinit var externalContentIntent: PendingIntent

        if (PlayerActivity.external) {
            val intent = Intent(baseContext, PlayerActivity::class.java)
            intent.putExtra("class","External")
            externalContentIntent = PendingIntent.getActivity(baseContext, 3, intent, PendingIntent.FLAG_IMMUTABLE)
        }


        val previousIntent = Intent(
            baseContext,
            NotificationReceiver::class.java
        ).setAction(ApplicationClass.PREVIOUS)
        val previousPendingIntent =
            PendingIntent.getBroadcast(baseContext, 0, previousIntent, PendingIntent.FLAG_IMMUTABLE)

        val nextIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent =
            PendingIntent.getBroadcast(baseContext, 1, nextIntent, PendingIntent.FLAG_IMMUTABLE)

        val playPauseIntent = Intent(
            baseContext,
            NotificationReceiver::class.java
        ).setAction(ApplicationClass.PLAY_PAUSE)
        val playPausePendingIntent = PendingIntent.getBroadcast(
            baseContext,
            2,
            playPauseIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val imageArt = getImageArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inSampleSize = 1
        //creating Bitmap from ByteArray
        val image = if (imageArt != null) {
            BitmapFactory.decodeByteArray(imageArt, 0, imageArt.size, options)
        } else {
            BitmapFactory.decodeResource(
                this@MusicService.resources,
                R.drawable.music_icon_notification
            )
        }

        val contentIntent =
            if (!PlayerActivity.external) mainContentIntent else externalContentIntent

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.music_icon_notification)
            .setLargeIcon(image)
            .addAction(R.drawable.previous_icon_notification, "Previous", previousPendingIntent)
            .addAction(playPauseButton, "Play", playPausePendingIntent)
            .addAction(R.drawable.next_icon_notification, "Next", nextPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(Uri.EMPTY)
            .setGroup(ApplicationClass.CHANNEL_ID)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0)
                    .setMediaSession(mediaSession.sessionToken)
            )
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val title = if(!PlayerActivity.external) PlayerActivity.musicListPA[PlayerActivity.songPosition].title else formatName().toString()
            mediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION,
                        mediaPlayer!!.duration.toLong()
                    )
                    .putBitmap(
                        MediaMetadataCompat.METADATA_KEY_ALBUM_ART, image
                    )
                    .putString(
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        PlayerActivity.musicListPA[PlayerActivity.songPosition].artist
                    )
                    .putString(
                        MediaMetadataCompat.METADATA_KEY_ALBUM,
                        PlayerActivity.musicListPA[PlayerActivity.songPosition].album
                    )
                    .putString(
                        MediaMetadataCompat.METADATA_KEY_TITLE,
                        title
                    )
                    .build()
            )

            mediaSession.setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setState(
                        currentState,
                        mediaPlayer!!.currentPosition.toLong(),
                        1f,
                        SystemClock.elapsedRealtime()
                    )
                    .setActions(
                        PlaybackStateCompat.ACTION_SEEK_TO or
                                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS

                    )
                    .build()
            )
            mediaSession.setCallback(PlayerMediaSessionCallback(mediaPlayer!!))
        }
        startForeground(7, notification)
    }


    fun createMediaPlayer(external: Boolean) {
        try {
            if (PlayerActivity.musicService!!.mediaPlayer == null) {
                PlayerActivity.musicService!!.mediaPlayer = MediaPlayer()
                createMediaPlayer(external)
            } else {
                PlayerActivity.musicService!!.mediaPlayer!!.reset()
                PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
                PlayerActivity.musicService!!.mediaPlayer!!.prepare()
                setLayout(baseContext, external)
                PlayerActivity.musicService!!.mediaPlayer!!.start()
                PlayerActivity.isSongPlaying = true
                PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
                showNotification(
                    R.drawable.pause_icon_notification,
                    PlaybackStateCompat.STATE_PLAYING
                )
                syncSeekBar()
                PlayerActivity.musicService!!.audioManager =
                    getSystemService(Context.AUDIO_SERVICE) as AudioManager
                @Suppress("DEPRECATION")
                PlayerActivity.musicService!!.audioManager.requestAudioFocus(
                    PlayerActivity.musicService,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }
        } catch (_: Exception) {
        }
    }

    fun syncSeekBar() {
        runnable = Runnable {
            PlayerActivity.binding.progressDuration.text =
                formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
    }

    override fun onAudioFocusChange(p0: Int) {
        if (p0 <= 0 && PlayerActivity.isSongPlaying) {
            PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.play_icon)
            showNotification(R.drawable.play_icon_notification, PlaybackStateCompat.STATE_PAUSED)
            NowPlaying.binding.nowPlayingPlayPause.setImageResource(R.drawable.play_icon_notification)
            PlayerActivity.isSongPlaying = false
            stopped = true
            mediaPlayer!!.pause()
        } else {
            if (stopped) {
                PlayerActivity.binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
                showNotification(
                    R.drawable.pause_icon_notification,
                    PlaybackStateCompat.STATE_PLAYING
                )
                NowPlaying.binding.nowPlayingPlayPause.setImageResource(R.drawable.pause_icon_notification)
                PlayerActivity.isSongPlaying = true
                mediaPlayer!!.start()
            }
        }

    }


}