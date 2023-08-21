package com.nizam.music_player

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nizam.music_player.databinding.ActivityPlayerBinding
import kotlin.random.Random

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var isSongPlaying = false
    companion object {
        var musicListPA = ArrayList<SongsData>()
        var songPosition = 0
        var mediaPlayer: MediaPlayer? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeLayout()

        playPauseSong()

        playNextSong()

        playPreviousSong()

        shuffleSong()
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index",0)

        when(intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA.addAll(MainActivity.musicListMA)
                createMediaPlayer()
            }

            "MainActivity" -> {
                musicListPA.addAll(MainActivity.musicListMA)
                songPosition = intent.getIntExtra("index",0)
                createMediaPlayer()
            }
        }
    }

    private fun shuffleSong() {
        binding.shuffleButton.setOnClickListener{
            songPosition = randomNumber()
            createMediaPlayer()
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
    private fun playPreviousSong() {
        binding.previousSong.setOnClickListener{
            if(songPosition == 0) {
                songPosition = musicListPA.size - 1
            } else {
                songPosition--
            }
            createMediaPlayer()
        }
    }

    private fun playNextSong() {
        binding.nextSong.setOnClickListener{
            if(songPosition == musicListPA.size - 1 ) {
                songPosition = 0
            } else {
                songPosition++
            }
            createMediaPlayer()
        }
    }

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createMediaPlayer() {
        try{

            setLayout()
            if(mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
                createMediaPlayer()
            } else {
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
                isSongPlaying = true
                binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
            }
        } catch(_:Exception) {}
    }
    private fun setLayout() {
        Glide.with(this@PlayerActivity)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(binding.albumImage)
        binding.songName.text = musicListPA[songPosition].title
    }

    private fun playMusic() {
        binding.pausePlayButton.setIconResource(R.drawable.pause_icon)
        isSongPlaying = true
        mediaPlayer!!.start()
    }
    private fun pauseMusic() {
        binding.pausePlayButton.setIconResource(R.drawable.play_icon)
        isSongPlaying = false
        mediaPlayer!!.pause()
    }
}