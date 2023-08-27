package com.nizam.music_player

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nizam.music_player.databinding.FavoritesRecyclerViewBinding

class PlayListRecyclerViewAdapter(private val context: Context,private val playList: ArrayList<PlayListData>):RecyclerView.Adapter<PlayListRecyclerViewAdapter.Holder>(){

    private val playListDB:PlaylistsDB by lazy {
        PlaylistsDB(context,null)
    }
    class Holder(binding: FavoritesRecyclerViewBinding):RecyclerView.ViewHolder(binding.root) {
        val playListName = binding.favoritesSongName
        val playListImage = binding.favoritesAlbumArt
        val root = binding.root
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        return Holder(FavoritesRecyclerViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.playListName.text = playList[position].playListName
        //setting image of album.
        Glide.with(context)
            .load(playList[position].playListImageArt)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(holder.playListImage)

        //Open Playlist
        openPlayList(holder, position)

        //delete Playlist
        holder.root.setOnLongClickListener{
            playListDB.removePlaylist(holder.playListName.text as String)
            PlaylistActivity.allPlaylist.remove(PlayListData(holder.playListName.text.toString(),null))
            notifyItemRemoved(position)
            true
        }
    }

    private fun openPlayList(holder: Holder, position: Int) {
        holder.root.setOnClickListener {
            Toast.makeText(context,playList[position].playListName,Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return playList.size
    }

}