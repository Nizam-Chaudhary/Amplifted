package com.nizam.music_player

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.nizam.music_player.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    companion object{

        lateinit var favoritesList:ArrayList<SongsData>
    }

    private lateinit var binding: ActivityFavoriteBinding
    private val userManager by lazy {
        UserManager(this)
    }
    private val favoritesDB: FavoritesDB by lazy {
        FavoritesDB(this@FavoriteActivity,null,userManager.getUserName())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding the layout
        binding = ActivityFavoriteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //setting title and back button on ToolBar.
        supportActionBar?.setTitle(R.string.favorites)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setRecyclerViewAdapter()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setRecyclerViewAdapter() {
        Log.i("Favorites","recyclerView")
        binding.favoritesRecyclerView.setHasFixedSize(true)
        binding.favoritesRecyclerView.setItemViewCacheSize(10)
        binding.favoritesRecyclerView.layoutManager = GridLayoutManager(this@FavoriteActivity,2)

        favoritesList = getSongData(favoritesDB)
        val favoritesRecyclerViewAdapter = FavoritesRecyclerViewAdapter(this@FavoriteActivity,favoritesList)
        binding.favoritesRecyclerView.adapter = favoritesRecyclerViewAdapter
    }

    override fun onResume() {
        super.onResume()
        if(favoritesList.size != getSongData(this.favoritesDB).size){
            setRecyclerViewAdapter()
        }
    }
}