package com.nizam.music_player

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shuffleButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }

        binding.favoritesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,FavoriteActivity::class.java))
        }

        binding.playlistsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }
    }
}