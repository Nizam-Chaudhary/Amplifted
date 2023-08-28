package com.nizam.music_player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nizam.music_player.databinding.ActivityAddSongsBinding

class AddSongsActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddSongsBinding
    private lateinit var playListName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playListName = intent.getStringExtra("playListName").toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add to PlayList"

        setRecyclerViewAdapter()
    }

    private fun setRecyclerViewAdapter() {
        binding.pickSongRecyclerView.setHasFixedSize(true)
        binding.pickSongRecyclerView.setItemViewCacheSize(13)
        binding.pickSongRecyclerView.layoutManager = LinearLayoutManager(this@AddSongsActivity)
        val adapter = AddSongRecyclerViewAdapter(this@AddSongsActivity,MainActivity.musicListMA,playListName)
        binding.pickSongRecyclerView.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}