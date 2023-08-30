package com.nizam.music_player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityRecentBinding

class RecentActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Recently Played"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}