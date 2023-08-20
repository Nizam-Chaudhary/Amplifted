package com.nizam.music_player

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import com.nizam.music_player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {

        //applying splash Screen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        val userManager = UserManager(this@MainActivity)
        if (!userManager.isUserLoggedIn()) {
            splashScreen.setKeepOnScreenCondition { true }
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
        setContentView(binding.root)
        updateOrRequestPermission()

        //For Navigation Drawer
        val drawerLayout:DrawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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