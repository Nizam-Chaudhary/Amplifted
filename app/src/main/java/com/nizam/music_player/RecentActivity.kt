package com.nizam.music_player

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        binding.recentRecyclerView.layoutManager = LinearLayoutManager(this@RecentActivity)
        musicListRP = recentDB.getAllSongs()
        val adapter = RecentRecyclerViewAdapter(this@RecentActivity, musicListRP)
        binding.recentRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recent_clear_menu,menu)
        menu?.findItem(R.id.clearRecent)?.setOnMenuItemClickListener {

            if (musicListRP.isNotEmpty())
                MaterialAlertDialogBuilder(this@RecentActivity)
                    .setTitle("Clear Recently Played!")
                    .setMessage("Do you want to clear recently played?")
                    .setPositiveButton("Yes") { _, _ ->
                        musicListRP.clear()
                        recentDB.clearRecent()
                        setAdapter()
                    }
                    .setNegativeButton("No") { _, _ -> }
                    .show()

            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}