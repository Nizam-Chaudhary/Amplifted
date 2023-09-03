package com.nizam.music_player

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nizam.music_player.MainActivity.Companion.main
import com.nizam.music_player.databinding.ActivityFavoriteBinding
import kotlin.random.Random

class FavoriteActivity : AppCompatActivity() {
    companion object{
        lateinit var favoritesList:ArrayList<SongsData>
    }

    private lateinit var binding: ActivityFavoriteBinding

    private val favoritesDB: FavoritesDB by lazy {
        FavoritesDB(this@FavoriteActivity,null)
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

        shuffleSong()
    }

    private fun shuffleSong() {
        binding.favoritesShuffle.setOnClickListener{
            if(favoritesList.size != 0) {
                main = true
                PlayerActivity.external = false
                val intent = Intent(this@FavoriteActivity,PlayerActivity::class.java)
                intent.putExtra("class","FavoriteActivity")
                intent.putExtra("index", Random.nextInt(0, favoritesList.size))
                startActivity(intent)
                PlayerActivity.shuffle = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setRecyclerViewAdapter() {
        binding.favoritesRecyclerView.setHasFixedSize(true)
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(this@FavoriteActivity)

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