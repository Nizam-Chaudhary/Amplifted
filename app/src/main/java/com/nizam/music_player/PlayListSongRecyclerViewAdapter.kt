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
import com.nizam.music_player.databinding.MusicRecyclerViewBinding

class PlayListSongRecyclerViewAdapter(private var context:Context,private var songsList:ArrayList<SongsData>) : RecyclerView.Adapter<PlayListSongRecyclerViewAdapter.Holder>() {

    private val playlistsDB:PlaylistsDB by lazy {
        PlaylistsDB(context,null)
    }

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
                .asBitmap()
                .load(songsList[position].artUri)
                .apply(RequestOptions().placeholder(R.drawable.icon).centerCrop())
                .into(holder.image)

            //PlaySongs
            playSong(holder,position)


        //delete Playlist
        holder.root.setOnLongClickListener{
            val dialog = MaterialAlertDialogBuilder(context)
                .setTitle("Remove Song from Playlist!")
                .setMessage("Do you want to Remove this song from ${PlayListSongsActivity.playListName}?")
                .setPositiveButton("Yes") {dialog,_ ->
                    val songToBeRemoved = playlistsDB.getPlaylistSongData(holder.title.text.toString(),PlayListSongsActivity.playListName)
                    playlistsDB.removeFromPlayList(PlayListSongsActivity.playListName,holder.title.text.toString())
                    notifyItemRemoved(PlayListSongsActivity.musicListPL.indexOf(songToBeRemoved))
                    PlayListSongsActivity.musicListPL.remove(songToBeRemoved)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
            true
        }
        }

    private fun playSong(holder: Holder, position: Int) {
        holder.root.setOnClickListener {
            sendIntent(position)
        }
    }

    private fun sendIntent(position: Int) {
        val intent = Intent(context,PlayerActivity::class.java)
        intent.putExtra("class","PlayList")
        intent.putExtra("index",position)
        ContextCompat.startActivity(context,intent,null)
    }
}
