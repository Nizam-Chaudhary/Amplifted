package com.nizam.music_player

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nizam.music_player.databinding.FavoritesRecyclerViewBinding

class FavoritesRecyclerViewAdapter(private var context:Context, private val songsList: ArrayList<SongsData>) : RecyclerView.Adapter<FavoritesRecyclerViewAdapter.Holder>() {
    class Holder(binding: FavoritesRecyclerViewBinding):RecyclerView.ViewHolder(binding.root) {
        val title = binding.favoritesSongName
        val albumArt = binding.favoritesAlbumArt
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FavoritesRecyclerViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //setting all values from arrayList into respective widgets
        holder.title.text = songsList[position].title
        holder.title.isSelected = true

        //setting image of album.
        Glide.with(context)
            .load(songsList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
            .into(holder.albumArt)

        //playing Song on PlayerActivity
        playSong(holder, position)
    }

    //this function plays songs on  PlayerActivity
    private fun playSong(
        holder: Holder,
        position: Int
    ) {
        holder.root.setOnClickListener {
            sendIntent(position)
        }
    }

    private fun sendIntent(position: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", position)
        intent.putExtra("class", "FavoritesAdapter")
        ContextCompat.startActivity(context, intent, null)
    }
}