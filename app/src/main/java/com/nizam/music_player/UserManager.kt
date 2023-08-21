package com.nizam.music_player

import android.content.Context

class UserManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val loggedInKey = "logged_in"
    private val userNameKey = "userName"

    //function to set user logged in Status
    fun setUserLoggedIn(loggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(loggedInKey, loggedIn)
        editor.apply()
    }

    //function to set userName
    fun setUserName(userName: String) {
        val editor = sharedPreferences.edit()
        editor.putString(userNameKey, userName)
        editor.apply()
    }

    //function to get User logged in Status.
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(loggedInKey, false)
    }

    //function to get userName from sharedPreferences.
    fun getUserName(): String {
        return sharedPreferences.getString(userNameKey, null).toString()
    }



}


