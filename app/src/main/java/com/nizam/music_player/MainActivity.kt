package com.nizam.music_player

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.nizam.music_player.databinding.ActivityMainBinding
import java.io.File
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicRecyclerViewAdapter: MusicRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        //applying splash Screen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        //checking if user is Logged in or Not if not then open Login Activity first.
        val userManager = UserManager(this@MainActivity)
        if (!userManager.isUserLoggedIn()) {
            splashScreen.setKeepOnScreenCondition { true }
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
        setContentView(binding.root)
        updateOrRequestPermission()

        musicListMA = getAllAudioFiles()

        //For Navigation Drawer
        toggle = ActionBarDrawerToggle(this@MainActivity,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //setting useName
        val headerView = binding.navView.getHeaderView(0)
        val userNameHeader: TextView = headerView.findViewById(R.id.userNameHeader)
        userNameHeader.text=userManager.getUserName()

        //On Click for menu drawer menu Item
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navSettings -> Toast.makeText(this@MainActivity,"Settings",Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(this@MainActivity,"About",Toast.LENGTH_SHORT).show()
                R.id.navExit -> finish()
            }
            true
        }

        //Checking for user logged in Status and If user Is logged Then he does not need to enter details and directly jump back to home Screen.
        binding.shuffleButton.setOnClickListener {
            val intent = Intent(this@MainActivity,PlayerActivity::class.java)
            intent.putExtra("index", Random.nextInt(0, musicListMA.size - 1))
            intent.putExtra("class","MainActivity")
            startActivity(intent)
        }

        binding.favoritesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,FavoriteActivity::class.java))
        }

        binding.playlistsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }

        binding.songsRecyclerView.setHasFixedSize(true)
        binding.songsRecyclerView.setItemViewCacheSize(20)
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        musicRecyclerViewAdapter = MusicRecyclerViewAdapter(this@MainActivity, musicListMA)
            binding.songsRecyclerView.adapter = musicRecyclerViewAdapter
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if(requestCode == 1) {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity,"Permission Granted",Toast.LENGTH_SHORT).show()
                } else {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_MEDIA_AUDIO),1)
                    Toast.makeText(this@MainActivity,"Permission Not Granted",Toast.LENGTH_SHORT).show()
                }
            }
        } else {

            if(requestCode == 2) {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity,"Permission Granted",Toast.LENGTH_SHORT).show()
                } else {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2)
                    Toast.makeText(this@MainActivity,"Permission Not Granted",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("Recycle", "Range")
    private fun getAllAudioFiles():ArrayList<SongsData> {
        val tempList = ArrayList<SongsData>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,MediaStore.Audio.Media.TITLE,null)
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
                }while (cursor.moveToNext())
            }
            cursor.close()
        }
        return tempList
    }

    companion object{
        lateinit var musicListMA: ArrayList<SongsData>
    }
}