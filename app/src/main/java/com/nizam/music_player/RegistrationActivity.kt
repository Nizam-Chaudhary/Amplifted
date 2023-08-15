package com.nizam.music_player

import DBLogin
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern


class RegistrationActivity : AppCompatActivity() {
    
    // Declared Objects to be used later for different widgets.
    private lateinit var uname: EditText
    private lateinit var uemail: EditText
    private lateinit var pwd: EditText
    private lateinit var retypedPwd: EditText
    private lateinit var register: Button
    private lateinit var alreadyRegistered: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)



        //Checking for user logged in Status and If user Is logged Then he does not need to enter details and directly jumpp back to home Screen.
        val userManager = UserManager(applicationContext)
        if(userManager.isUserLoggedIn()) {
            intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }

        //Assigned id of all widgets to declared Objects.
        uname = findViewById(R.id.userName_edtxt)
        uemail = findViewById(R.id.email_edtxt)
        pwd = findViewById(R.id.pwd_edtxt)
        retypedPwd = findViewById(R.id.retypepwd_edtxt)

        register = findViewById(R.id.register_btn)
        alreadyRegistered = findViewById(R.id.alreadyRegistered_btn)

        //OnClick Listner to change activity to login Screen if user is Already Registered.
        alreadyRegistered.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(Intent(applicationContext,MainActivity::class.java))
            }
        })

        /*OnClick Listener for Register Button.
          it Saves Data to Database checking Various validation put on EditText Fields to enusre proper Data.
          On Succesfull Registration User is then presented with Home Screen.
        */

        register.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // variable flag is to be used for checking various validations on EditText Field for proper data.
                var flag = true

                //created Login DataBase Object.
                val db = DBLogin(applicationContext,null)

                //Assigned EditText Values into variables.
                val name = uname.text.toString()
                val email = uemail.text.toString()
                val passwd = pwd.text.toString()
                val rePasswd = retypedPwd.text.toString()

                //validation for userName Field.
                if(name.length < 5) {
                    flag = false
                    uname.error = "Must Contain 5 or more characters"
                }

                //Regex for verfication of Email.
                val emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.[A-Za-z]{2,4}$"

                //This Block of code matches pattern with email entered and if the value is not proper then we get an error Regarding it to notify the User.
                if(!Pattern.matches(emailRegex,email)) {
                    flag = false
                    uemail.error = "Enter Proper Email"
                }

                //Validation for Password Length
                if(passwd.length < 8 ) {
                    flag = false
                    pwd.error = "Must Contain 8 or more characters"
                }

                //checking that password is not equal to User Name for Security Purpose.
                if(passwd == name) {
                    flag = false
                    pwd.error = "User Name and Password Must be different"
                }

                //Checking if both passwords are matching.
                if( passwd != rePasswd) {
                    flag = false
                    retypedPwd.error = "Passwords Not Matching"
                }

                //Checking if User Already Exists.
                if(db.isAlreadyRegistered(name)) {
                    flag = false

                    //dialog will be Displayed to notify the user.
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@RegistrationActivity)
                    builder.setTitle("Alert")
                    builder.setPositiveButton("Ok", {dialog, _ ->
                        dialog.dismiss()
                    })
                    builder.setMessage("Account with the given UserName is already registered.")
                    val dialog = builder.create()
                    dialog.show()

                    //All EditText Fields will be clear so that new name and values can be entered.
                    uname.text.clear()
                    uemail.text.clear()
                    pwd.text.clear()
                    retypedPwd.text.clear()
                }
                //If all the above Validations are succesfully completed then the below block of code is Executed and User is Registered.
                if( flag) {
                    try {
                        db.registerUser(name, email, passwd)
                    }
                    catch (e :Exception) {

                    }

                    //setting User Logged IN Status.
                    userManager.setUserLoggedIn(true)
                    Toast.makeText(applicationContext,"User Registered Successfully",Toast.LENGTH_SHORT).show()

                    //After Registration Homepage is Opened.
                    intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }
}