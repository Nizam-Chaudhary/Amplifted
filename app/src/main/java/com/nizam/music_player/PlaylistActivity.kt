package com.nizam.music_player

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nizam.music_player.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding

    private val playListDB:PlaylistsDB by lazy {
        PlaylistsDB(this@PlaylistActivity,null)
    }

    companion object{
        lateinit var allPlaylist:ArrayList<PlayListData>


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //setting title and back button on ToolBar.
        supportActionBar?.setTitle(R.string.playlists)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        setRecyclerViewAdapter()

        addPlayList()
    }

    private fun addPlayList() {
        binding.addPlaylist.setOnClickListener {
            createDialog()
        }
    }

    private fun createDialog() {
        val playListName = EditText(this@PlaylistActivity)
        playListName.hint = "Enter Playlist Name"
        val linearLayout = LinearLayout(this@PlaylistActivity)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(50,30,50,30)
        linearLayout.addView(playListName)
        val dialog = MaterialAlertDialogBuilder(this@PlaylistActivity)
            .setView(linearLayout)
            .setTitle("Create PlayList")
            .setPositiveButton("Ok") {_,_ ->
                playListDB.createPlayList(playListName.text.toString())
                setRecyclerViewAdapter()
            }
            .setNegativeButton("Cancel") {dialog,_ ->
                dialog.dismiss()
            }
        dialog.show()
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setRecyclerViewAdapter() {
        binding.playListRecyclerView.setHasFixedSize(true)
        allPlaylist = ArrayList()
        allPlaylist = getAllPlayList()
        binding.playListRecyclerView.layoutManager = GridLayoutManager(this@PlaylistActivity,3)
        val playListRecyclerViewAdapter = PlayListRecyclerViewAdapter(this@PlaylistActivity,
            allPlaylist)
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

    override fun onResume() {
        super.onResume()
        allPlaylist = getAllPlayList()
        val playListRecyclerViewAdapter = PlayListRecyclerViewAdapter(this@PlaylistActivity,
            allPlaylist)
        binding.playListRecyclerView.adapter = playListRecyclerViewAdapter
    }
}