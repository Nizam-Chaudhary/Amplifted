package com.nizam.music_player

import DBLogin
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var uname: EditText
    private lateinit var uemail: EditText
    private lateinit var pwd: EditText
    private lateinit var retypedPwd: EditText
    private lateinit var register: Button
    private lateinit var alreadyRegistered: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val userManager:UserManager = UserManager(applicationContext)
        if(userManager.isUserLoggedIn()) {
            intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        } else {
            println("GoodBye")
        }
        uname = findViewById(R.id.userName_edtxt)
        uemail = findViewById(R.id.email_edtxt)
        pwd = findViewById(R.id.pwd_edtxt)
        retypedPwd = findViewById(R.id.retypepwd_edtxt)

        register = findViewById(R.id.register_btn)
        alreadyRegistered = findViewById(R.id.alreadyRegistered_btn)

        alreadyRegistered.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(Intent(applicationContext,MainActivity::class.java))
            }
        })

        register.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val db = DBLogin(applicationContext,null)

                val name = uname.text.toString()
                val email = uemail.text.toString()
                val passwd = pwd.text.toString()
                val rePasswd = retypedPwd.text.toString()
                if( passwd == rePasswd) {
                    try {
                        db.registerUser(name, email, passwd)
                    }
                    catch (e :Exception) {

                    }
                    userManager.setUserLoggedIn(true)
                    Toast.makeText(applicationContext,"User Registered Successfully",Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(applicationContext,"Passwords are not Matching Enter Correctly",Toast.LENGTH_SHORT).show()
                    retypedPwd.error = "Passwords Not Matching"
                }
            }
        })
    }
}