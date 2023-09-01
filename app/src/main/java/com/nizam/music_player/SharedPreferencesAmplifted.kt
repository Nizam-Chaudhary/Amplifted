package com.nizam.music_player

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesAmplifted(val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE)
    private val sortByKey = "SortBy"

    fun setSortBy(sortBy: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(sortByKey,sortBy)
        editor.apply()
    }

    fun getSortBy() : Int {
        return sharedPreferences.getInt(sortByKey,1)
    }
}