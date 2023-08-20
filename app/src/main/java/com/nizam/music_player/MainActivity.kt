package com.nizam.music_player

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nizam.music_player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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

        //Checking for user logged in Status and If user Is logged Then he does not need to enter details and directly jump back to home Screen.
        binding.shuffleButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }

        binding.favoritesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,FavoriteActivity::class.java))
        }

        binding.playlistsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,PlaylistActivity::class.java))
        }
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