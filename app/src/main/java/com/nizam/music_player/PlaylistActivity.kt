package com.nizam.music_player

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.nizam.music_player.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding

    private val playListDB:PlaylistsDB by lazy {
       PlaylistsDB(this@PlaylistActivity,null)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //setting title and back button on ToolBar.
        supportActionBar?.setTitle(R.string.playlists)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setRecyclerViewAdapter() {
        Log.i("Favorites","recyclerView")
        binding.playListRecyclerView.setHasFixedSize(true)
        binding.playListRecyclerView.setItemViewCacheSize(10)
        binding.playListRecyclerView.layoutManager = GridLayoutManager(this@PlaylistActivity,2)

        val playListRecyclerViewAdapter = PlayListRecyclerViewAdapter(this@PlaylistActivity,getAllPlayList())
        binding.playListRecyclerView.adapter = playListRecyclerViewAdapter
    }

    private fun getAllPlayList():ArrayList<PlayListData> {
        val list:ArrayList<PlayListData> = ArrayList()
        val tempList = playListDB.getPlayListNames()

        for(i in tempList) {
            list.add(PlayListData(i,playListDB.getPlayListArtUri(i)))
        }

        return list
    }
}