package com.nizam.music_player

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //OnClickListener for Register Button to open Registration Page to Register The user if not Registered Already.
        var registerbtn: Button = findViewById(R.id.register_btn)
        registerbtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
            }
        })
    }
}