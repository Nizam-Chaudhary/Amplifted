package com.nizam.music_player

import android.content.Context

class UserManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val loggedInKey = "logged_in"
    private val userNameKey = "userName"

    fun setUserLoggedIn(loggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(loggedInKey, loggedIn)
        editor.apply()
    }

    fun setUserName(userName: String) {
        val editor = sharedPreferences.edit()
        editor.putString(userNameKey, userName)
        editor.apply()
    }


    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(loggedInKey, false)
    }

    fun getUserName(): String {
        return sharedPreferences.getString(userNameKey, null).toString()
    }



}


