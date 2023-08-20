package com.nizam.music_player

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nizam.music_player.databinding.MusicRecyclerViewBinding

class MusicRecyclerViewAdapter(private var context: Context, private var songsList: ArrayList<SongsData>):RecyclerView.Adapter<MusicRecyclerViewAdapter.Holder>(){
    class Holder(binding:MusicRecyclerViewBinding):RecyclerView.ViewHolder(binding.root) {
        val title = binding.songName
        val album= binding.albumName
        val duration = binding.songPlayTime
        val image = binding.songImageView
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
        holder.duration.text =formatDuration(songsList[position].duration)// (songsList[position].duration/1000).toString()
        Glide.with(context)
            .load(songsList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(holder.image)
    }
}