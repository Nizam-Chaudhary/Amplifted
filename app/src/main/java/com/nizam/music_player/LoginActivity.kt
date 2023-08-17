package com.nizam.music_player

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Defined and find View Id.
        val uname : EditText = findViewById(R.id.userName_edtxt)
        val pwd : EditText = findViewById(R.id.pwd_edtxt)
        val login : Button = findViewById(R.id.login_btn)

        //onClickListener for Login Button to Login To Application If all validations are passed.
        login.setOnClickListener {


            val db = DBLogin(this@LoginActivity,null)

            val name = uname.text.toString()
            val passwd = pwd.text.toString()

            //Checking if User is Available or not if not give error.
            if(!db.isAvailable(name)) {
                uname.error = "No User is available with this Name"
            } else {
                //Checking password is correct or not.
                if(db.validateUser(name,passwd)) {
                    val userManager = UserManager(this@LoginActivity)

                    //Setting User Logged In status and username.
                    userManager.setUserLoggedIn(true)
                    userManager.setUserName(name)

                    Toast.makeText(this@LoginActivity,"Logged In Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    //error on incorrect password.
                    pwd.error = "Incorrect Password"
                }
            }
        }

        //OnClickListener for Register Button to open Registration Page to Register The user if not Registered Already.
        val registerBtn: Button = findViewById(R.id.register_btn)

        registerBtn.setOnClickListener {
            intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}