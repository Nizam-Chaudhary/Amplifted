package com.nizam.music_player

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nizam.music_player.databinding.PlaylistsRecyclerViewBinding

class PlayListRecyclerViewAdapter(private val context: Context,private val playList: ArrayList<PlayListData>):RecyclerView.Adapter<PlayListRecyclerViewAdapter.Holder>(){

    private val playListDB:PlaylistsDB by lazy {
        PlaylistsDB(context,null)
    }
    class Holder(binding: PlaylistsRecyclerViewBinding):RecyclerView.ViewHolder(binding.root) {
        val playListName = binding.favoritesSongName
        val playListImage = binding.favoritesAlbumArt
        val root = binding.root
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        return Holder(PlaylistsRecyclerViewBinding.inflate(LayoutInflater.from(context),parent,false))
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
            val dialog = MaterialAlertDialogBuilder(context)
                .setTitle("Delete Playlist!")
                .setMessage("Do you want to delete this Playlist?")
                .setPositiveButton("Yes") {dialog,_ ->
                    val songToBeRemoved = PlayListData(holder.playListName.text.toString(),playListDB.getPlayListArtUri(holder.playListName.text.toString()))
                    playListDB.removePlaylist(holder.playListName.text as String)
                    notifyItemRemoved(PlaylistActivity.allPlaylist.indexOf(songToBeRemoved))
                    PlaylistActivity.allPlaylist.remove(songToBeRemoved)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
            true
        }
    }

    private fun openPlayList(holder: Holder, position: Int) {
        holder.root.setOnClickListener {
            val intent = Intent(context,PlayListSongsActivity::class.java)
            intent.putExtra("playListName",playList[position].playListName)
            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return playList.size
    }

}