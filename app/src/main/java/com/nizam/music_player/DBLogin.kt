package com.nizam.music_player

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBLogin(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            ("CREATE TABLE $TABLE_NAME ($NAME_COL TEXT Primary Key, $EMAIL_COL TEXT NOT NULL, $PWD_COL TEXT NOT NULL)")
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("Drop Table if exists $TABLE_NAME")
        onCreate(db)
    }

    fun registerUser(uname: String, uEmail: String, pwd: String) {
        val values = ContentValues()

        values.put(NAME_COL, uname)
        values.put(EMAIL_COL, uEmail)
        values.put(PWD_COL, pwd)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    @SuppressLint("Range")
    fun helper(name: String): String? {
        val db = this.readableDatabase
        val query = "select $PWD_COL from $TABLE_NAME where $NAME_COL = '$name'"
        val cursor = db.rawQuery(query, null)
        var value: String? = null
        if (cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndex(PWD_COL))
        }
        cursor.close()
        return value
    }

    fun validateUser(name: String, pwd: String): Boolean {
        val result = helper(name)
        if (result != null && result == pwd) {
            return true
        }
        return false
    }

    //This function can be used check if user is already Registered and
    //Also to check user is registered or not on Login Page.
    fun isAvailable(name: String): Boolean {
        val result = helper(name)
        if (result != null) {
            return true
        }
        return false
    }


    companion object {
        const val DATABASE_NAME = "UserDB"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "UserMaster"
        const val NAME_COL = "uname"
        const val EMAIL_COL = "email"
        const val PWD_COL = "pwd"
    }
}