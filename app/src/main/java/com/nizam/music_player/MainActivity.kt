package com.nizam.music_player

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userManager:UserManager = UserManager(applicationContext)

        val btn = findViewById(R.id.testbtn) as Button
        btn.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(applicationContext,RegistrationActivity::class.java))
                userManager.setUserLoggedIn(false)
            }
        })
    }
}