package com.nizam.music_player

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nizam.music_player.databinding.MusicRecyclerViewBinding

class MusicRecyclerViewAdapter(private var context: Context, private var songsList: ArrayList<SongsData>):RecyclerView.Adapter<MusicRecyclerViewAdapter.Holder>(){
    class Holder(binding:MusicRecyclerViewBinding):RecyclerView.ViewHolder(binding.root) {
        val title = binding.songName
        val album= binding.albumName
        val image = binding.songImageView
        val duration = binding.songPlayTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(MusicRecyclerViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = songsList[position].title
        holder.album.text = songsList[position].album
        holder.duration.text = (songsList[position].duration/3600).toString()

    }
}