package com.nizam.music_player

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityPlayerBinding
import kotlin.random.Random

class PlayerActivity : AppCompatActivity(),ServiceConnection,MediaPlayer.OnCompletionListener{

    companion object {
        var isSongPlaying = false
        var musicListPA = ArrayList<SongsData>()
        var songPosition = 0
        var musicService:MusicService? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //for starting service
        val intent = Intent(this@PlayerActivity,MusicService::class.java)
        bindService(intent,this@PlayerActivity, BIND_AUTO_CREATE)
        startService(intent)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeLayout()

        playPauseSong()

        nextSong()

        previousSong()

        shuffleSong()



        binding.seekBarPA.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2) musicService!!.mediaPlayer!!.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })

    }

    private fun previousSong() {
        binding.previousSong.setOnClickListener{
            playPreviousSong()
            musicService!!.createMediaPlayer()
        }
    }

    private fun nextSong() {
        binding.nextSong.setOnClickListener{
            playNextSong()
            musicService!!.createMediaPlayer()
        }
    }

    //Initializes layout and all variable and retrieves the value from intent.
    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index",0)

        when(intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA.addAll(MainActivity.musicListMA)
            }

            "MainActivity" -> {
                musicListPA.addAll(MainActivity.musicListMA)
                songPosition = intent.getIntExtra("index",0)
            }
        }
    }

    //this function generates an random index and shuffles the song.
    private fun shuffleSong() {
        binding.shuffleButton.setOnClickListener{
            songPosition = randomNumber()
            musicService!!.createMediaPlayer()
        }
    }

    //generates Random number other than current song playing.
    private fun randomNumber():Int {
        var temp = Random.nextInt(0, musicListPA.size)
        while (temp == songPosition) {
            temp = Random.nextInt(
                0,
                musicListPA.size
            )
            if(temp != songPosition) {
                break
            }
        }
        return temp
    }

    //this function is used to play the previous song and it responds to previousSongButton.

    //plays or Pauses the song..
    private fun playPauseSong() {
        //Pause Or Play Button OnClickListener
        binding.pausePlayButton.setOnClickListener{
            if(isSongPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }
    }

    //this is called when the user clicks on Back Button.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    //this function initializes Media-player if it is null and starts playing the song if it is already initialized.


    //setting song name and album image to the song.


    //plays the songs if it is paused.
    private fun playMusic() {
        binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
        musicService!!.showNotification(R.drawable.pause_icon_notification)
        isSongPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    //pauses the songs if it is playing.
    private fun pauseMusic() {
        binding.pausePlayButton.setIconResource(R.drawable.play_icon)
        musicService!!.showNotification(R.drawable.play_icon_notification)
        isSongPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        musicService!!.createMediaPlayer()
        musicService!!.mediaPlayer!!.setOnCompletionListener(this)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(p0: MediaPlayer?) {
        playNextSong()
        musicService!!.createMediaPlayer()
    }
}