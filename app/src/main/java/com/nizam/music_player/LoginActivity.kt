package com.nizam.music_player

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginUser()

        notRegistered()
    }

    //this function open Registration page if user is not registered.
    private fun notRegistered() {
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            finish()
        }
    }

    //this function checks all validations and log's in the user if fields are valid.
    private fun loginUser() {
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
                    finish()
                } else {
                    //error on incorrect password.
                    binding.pwdEdtxt.error = "Incorrect Password"
                }
            }
        }
    }
}