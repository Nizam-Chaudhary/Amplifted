package com.nizam.music_player

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {

    companion object {
        var musicListPA = ArrayList<SongsData>()
        var songPosition = 0
        var mediaPlayer: MediaPlayer? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        songPosition = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA.addAll(MainActivity.musicListMA)
                if(mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                } else {
                    mediaPlayer!!.reset()
                    mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                }

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}