package com.nizam.music_player

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nizam.music_player.databinding.MusicRecyclerViewBinding

class AddSongRecyclerViewAdapter(private var context: Context, private var songsList: ArrayList<SongsData>,var playListName: String):
    RecyclerView.Adapter<AddSongRecyclerViewAdapter.Holder>(){
    class Holder(binding: MusicRecyclerViewBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.songName
        val album= binding.albumName
        val duration = binding.songPlayTime
        val image = binding.songImageView
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(MusicRecyclerViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //setting all values from arrayList into respective widgets
        holder.title.text = songsList[position].title
        holder.album.text = songsList[position].album
        holder.duration.text =formatDuration(songsList[position].duration)

        //setting image of album.
        Glide.with(context)
            .load(songsList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(holder.image)

        addSong(holder,position)
    }

    private fun addSong(holder: Holder, position: Int) {
        holder.root.setOnClickListener {
            val playListDB = PlaylistsDB(context,null)
            playListDB.addToPlaylist(songsList[position],playListName)
            val intent=Intent(context,PlayListSongsActivity::class.java)
            intent.putExtra("playListName",playListName)
            ContextCompat.startActivity(context, intent,null)
        }
    }
}