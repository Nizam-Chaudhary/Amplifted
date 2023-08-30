package com.nizam.music_player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nizam.music_player.databinding.ActivityRecentBinding

class RecentActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecentBinding
    private val recentDB: RecentDB by lazy {
        RecentDB(this@RecentActivity,null)
    }
    companion object {
        var musicListRP: ArrayList<SongsData> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Recently Played"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setAdapter()
    }

    private fun setAdapter() {
        binding.recentRecyclerView.setHasFixedSize(true)
        binding.recentRecyclerView.setItemViewCacheSize(13)
        binding.recentRecyclerView.layoutManager = LinearLayoutManager(this@RecentActivity)
        musicListRP = recentDB.getAllSongs()
        val adapter = RecentRecyclerViewAdapter(this@RecentActivity, musicListRP)
        binding.recentRecyclerView.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}