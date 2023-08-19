package com.nizam.music_player

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //onClickListener for Login Button to Login To Application If all validations are passed.
        binding.loginBtn.setOnClickListener {


            val db = DBLogin(this@LoginActivity, null)

            val name = binding.userNameEdtxt.text.toString()
            val passwd = binding.pwdEdtxt.text.toString()

            //Checking if User is Available or not if not give error.
            if (!db.isAvailable(name)) {
                binding.userNameEdtxt.error = "No User is available with this Name"
            } else {
                //Checking password is correct or not.
                if (db.validateUser(name, passwd)) {
                    val userManager = UserManager(this@LoginActivity)

                    //Setting User Logged In status and username.
                    userManager.setUserLoggedIn(true)
                    userManager.setUserName(name)

                    Toast.makeText(this@LoginActivity, "Logged In Successfully", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    //error on incorrect password.
                    binding.pwdEdtxt.error = "Incorrect Password"
                }
            }
        }

        //OnClickListener for Register Button to open Registration Page to Register The user if not Registered Already.
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }
}