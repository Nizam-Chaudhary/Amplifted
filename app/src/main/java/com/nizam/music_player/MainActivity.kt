package com.nizam.music_player

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.nizam.music_player.databinding.ActivityMainBinding

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
        binding.navView.setNavigationItemSelectedListener { it ->
            when(it.itemId) {
                R.id.navSettings -> Toast.makeText(this@MainActivity,"Settings",Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(this@MainActivity,"About",Toast.LENGTH_SHORT).show()
                R.id.navExit -> finish()
            }
            true
        }

        //Checking for user logged in Status and If user Is logged Then he does not need to enter details and directly jump back to home Screen.
        binding.shuffleButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlayerActivity::class.java))
        }

        binding.favoritesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,FavoriteActivity::class.java))
        }

        binding.playlistsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }

        val songsList = ArrayList<String>()
        songsList.add("1 Song")
        songsList.add("2 Song")
        songsList.add("3 Song")
        songsList.add("4 Song")
        songsList.add("5 Song")
        binding.songsRecyclerView.setHasFixedSize(true)
        binding.songsRecyclerView.setItemViewCacheSize(20)
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        musicRecyclerViewAdapter = MusicRecyclerViewAdapter(this@MainActivity,songsList)
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
}