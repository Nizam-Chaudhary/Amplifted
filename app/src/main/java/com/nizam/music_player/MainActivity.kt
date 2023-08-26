package com.nizam.music_player

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nizam.music_player.databinding.ActivityMainBinding
import java.io.File
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicRecyclerViewAdapter: MusicRecyclerViewAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var splashScreen: androidx.core.splashscreen.SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {

        //applying splash Screen
        splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        //initializing binding variable
        binding = ActivityMainBinding.inflate(layoutInflater)

        checkAppHasPermission()

        if(intent.getBooleanExtra("EXIT",false)) finish()

        setContentView(binding.root)

        search = false

        setToggleButtonForNavigationDrawer()

        setUserNameOnDrawerHeader()

        navMenuItemClick()

        shuffleSong()

        binding.favoritesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,FavoriteActivity::class.java))
        }

        binding.playlistsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }

        checkAndSetAdapter()

        refreshLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu,menu)
        val searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText != null) {
                    musicListSearched = ArrayList()
                    val userInput = newText.lowercase()
                    for(song in musicListMA) {
                        if(song.title.lowercase().contains(userInput))
                            musicListSearched.add(song)
                    }
                    search = true
                    musicRecyclerViewAdapter.updateMusicList(musicListSearched)
                }

                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    //This function is used to check if the app has permission or not.
    private fun checkAppHasPermission() {
        hasPermission = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        else
            ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        //if app does not has permission then requesting for it.
        if(!hasPermission)
            updateOrRequestPermission()
    }

    //this function checks if app has permission call function setRecyclerViewAdapter()  to set the adapter.
    private fun checkAndSetAdapter() {
        if(hasPermission) {
            musicListMA = getAllAudioFiles()
            setRecyclerViewAdapter()
        }
    }


    //this function sets the adapter.
    private fun setRecyclerViewAdapter() {
        binding.songsRecyclerView.setHasFixedSize(true)
        binding.songsRecyclerView.setItemViewCacheSize(20)
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        musicRecyclerViewAdapter = MusicRecyclerViewAdapter(this@MainActivity, musicListMA)
        binding.songsRecyclerView.adapter = musicRecyclerViewAdapter
    }

    //this function creates an random index from the list of songs pass it start playing random song.
    private fun shuffleSong() {
        //Checking for user logged in Status and If user Is logged Then he does not need to enter details and directly jump back to home Screen.
        binding.shuffleButton.setOnClickListener {
            if(musicListMA.size != 0) {
                val intent = Intent(this@MainActivity,PlayerActivity::class.java)
                intent.putExtra("index", Random.nextInt(0, musicListMA.size))
                intent.putExtra("class","MainActivity")
                startActivity(intent)
                PlayerActivity.shuffle = true
            } else {
                Toast.makeText(this@MainActivity,"No Songs Available",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navMenuItemClick() {
        //On Click for menu drawer menu Item
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navSettings -> Toast.makeText(this@MainActivity,"Settings",Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(this@MainActivity,"About",Toast.LENGTH_SHORT).show()
                R.id.navExit -> {
                    val dialog = MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Exit!")
                        .setCancelable(false)
                        .setMessage("Do you want to Close the App?")
                        .setPositiveButton("Yes") { _, _ ->
                            finish()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                    dialog.show()
                }
            }
            true
        }
    }

    //this function is used to set the username on the DrawerHeader.
    private fun setUserNameOnDrawerHeader() {
        //setting useName
        //val headerView = binding.navView.getHeaderView(0)
        //val userNameHeader: TextView = headerView.findViewById(R.id.userNameHeader)
    }

    //this function set toggle button for DrawerLayout
    private fun setToggleButtonForNavigationDrawer() {
        //For Navigation Drawer
        toggle = ActionBarDrawerToggle(this@MainActivity,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //For Requesting Runtime Permission.

    private fun updateOrRequestPermission() {
        if(ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_DENIED ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_MEDIA_AUDIO),1)
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if(ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ){
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2)
            }
        }


    }

    //onResponse to the permission Dialog an action is performed.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //checking android api version.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(requestCode == 1) {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermission = true
                    checkAndSetAdapter()
                } else {
                    startActivity(Intent(this@MainActivity,RequestPermission::class.java))
                    finish()
                }
            }
        } else {
            if(requestCode == 2) {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermission = true
                    checkAndSetAdapter()
                } else {
                    startActivity(Intent(this@MainActivity,RequestPermission::class.java))
                    finish()
                }
            }
        }
    }

    //this function is used to load all files into ArrayList from the Storage
    @SuppressLint("Recycle", "Range")
    private fun getAllAudioFiles():ArrayList<SongsData> {
        val tempList = ArrayList<SongsData>()
        //selection refers to the type of file to fetch
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        //projection refers to the information need in form of array.
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        //loading all data into cursor.
        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,MediaStore.Audio.Media.TITLE,null)
        //if cursor is not null then adding all values to tempList.
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri,albumIdC).toString()
                    val music = SongsData(id = idC,title = titleC, album = albumC,duration = durationC, path = pathC, artist = artistC, artUri = artUriC)
                    val file = File(music.path)
                    if(file.exists()) {
                        tempList.add(music)
                    }
                }while(cursor.moveToNext())
            }
            cursor.close()
        }
        return tempList
    }

    //refreshes the layout to get changes on song list.
    private fun refreshLayout(){
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing=true
            checkAndSetAdapter()
            binding.refreshLayout.isRefreshing=false
        }
    }
    companion object{
        lateinit var musicListMA: ArrayList<SongsData>
        var hasPermission = false
        lateinit var musicListSearched: ArrayList<SongsData>
        var search = false
    }

    override fun onDestroy() {

        super.onDestroy()
        if(PlayerActivity.musicService != null) {
            if(PlayerActivity.isSongPlaying) {
                PlayerActivity.musicService!!.mediaPlayer!!.stop()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                PlayerActivity.musicService!!.stopForeground(Service.STOP_FOREGROUND_REMOVE)
            }
        }
        PlayerActivity.musicService = null
    }

}