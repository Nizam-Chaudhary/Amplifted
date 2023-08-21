package com.nizam.music_player

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityRegistrationBinding
import java.util.regex.Pattern


class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var userManager:UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        userManager = UserManager(this@RegistrationActivity)

        alreadyRegistered()

        registerUser()
    }

    private fun alreadyRegistered() {
        //OnClick Listener to change activity to login Screen if user is Already Registered.
        binding.alreadyRegisteredBtn.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
    }

    private fun registerUser(
    ) {
        /*OnClick Listener for Register Button.
          it Saves Data to Database checking Various validation put on EditText Fields to ensure proper Data.
          On Successful Registration User is then presented with Home Screen.
        */
        binding.registerBtn.setOnClickListener {
            // variable flag is to be used for checking various validations on EditText Field for proper data.
            var flag = true

            //created Login DataBase Object.
            val db = DBLogin(applicationContext, null)

            //Assigned EditText Values into variables.
            val name = binding.userNameEdtxt.text.toString()
            val email = binding.emailEdtxt.text.toString()
            val passwd = binding.pwdEdtxt.text.toString()
            val rePasswd = binding.retypepwdEdtxt.text.toString()

            //validation for userName Field.
            if (name.length < 5) {
                flag = false
                binding.userNameEdtxt.error = "Must Contain 5 or more characters"
            }

            //Regex for verification of Email.
            val emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.[A-Za-z]{2,4}$"

            //This Block of code matches pattern with email entered and if the value is not proper then we get an error Regarding it to notify the User.
            if (!Pattern.matches(emailRegex, email)) {
                flag = false
                binding.emailEdtxt.error = "Enter Proper Email"
            }

            //Validation for Password Length
            if (passwd.length < 8) {
                flag = false
                binding.pwdEdtxt.error = "Must Contain 8 or more characters"
            }

            //checking that password is not equal to User Name for Security Purpose.
            if (passwd == name) {
                flag = false
                binding.pwdEdtxt.error = "User Name and Password Must be different"
            }

            //Checking if both passwords are matching.
            if (passwd != rePasswd) {
                flag = false
                binding.retypepwdEdtxt.error = "Passwords Not Matching"
            }

            //Checking if User Already Exists.
            if (db.isAvailable(name)) {
                flag = false

                dialogUserAlreadyExists()

                //All EditText Fields will be clear so that new name and values can be entered.
                binding.userNameEdtxt.text.clear()
                binding.emailEdtxt.text.clear()
                binding.pwdEdtxt.text.clear()
                binding.retypepwdEdtxt.text.clear()
            }
            //If all the above Validations are successfully completed then the below block of code is Executed and User is Registered.
            if (flag) {
                try {
                    db.registerUser(name, email, passwd)
                } catch (_: Exception) {
                }

                setUserStatus(name)

                Toast.makeText(
                    applicationContext,
                    "User Registered Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                //After Registration Homepage is Opened.
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setUserStatus(name: String) {
        //setting User Logged IN Status.
        userManager.setUserLoggedIn(true)
        //setting User Name to sharedPreferences for future use
        userManager.setUserName(name)
    }

    private fun dialogUserAlreadyExists() {
        //dialog will be Displayed to notify the user.
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@RegistrationActivity)
        builder.setTitle("Alert")
        builder.setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setMessage("Account with the given UserName is already registered.")
        val dialog = builder.create()
        dialog.show()
    }
}