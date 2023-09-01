package com.nizam.music_player

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavoritesDB(val context: Context, factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context,
    DATABASE_NAME,factory, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "Favorites"
        const val DATABASE_VERSION = 1

        const val ID_COL = "id"
        const val TITLE_COL = "title"
        const val ALBUM_COL = "album"
        const val ARTIST_COL = "artist"
        const val DURATION_COL = "duration"
        const val PATH_COL = "path"
        const val ART_URI_COL = "artUri"
    }


    override fun onCreate(p0: SQLiteDatabase?) {
        val query = "CREATE TABLE IF NOT EXISTS Favorites($ID_COL TEXT PRIMARY KEY, $TITLE_COL TEXT, $ALBUM_COL TEXT, $ARTIST_COL TEXT, $DURATION_COL INTEGER, $PATH_COL TEXT, $ART_URI_COL TEXT)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS Favorites")
        onCreate(p0)
    }

    fun addToFavorites(songsData: SongsData) {
        val values = ContentValues()

        values.put(ID_COL,songsData.id)
        values.put(TITLE_COL,songsData.title)
        values.put(ALBUM_COL,songsData.album)
        values.put(ARTIST_COL,songsData.artist)
        values.put(DURATION_COL,songsData.duration)
        values.put(PATH_COL,songsData.path)
        values.put(ART_URI_COL,songsData.artUri)

        val db = this.writableDatabase
        db.insert("Favorites",null,values)

        db.close()
    }

    @SuppressLint("Range")
    fun songExists(name: String): Boolean {
        val db = this.readableDatabase

        val query = "SELECT $ID_COL FROM Favorites WHERE $TITLE_COL = '$name'"
        val cursor = db.rawQuery(query,null)
        var value: String? = null
        if(cursor.moveToFirst())
            value = cursor.getString(cursor.getColumnIndex(ID_COL))
        cursor.close()
        if(value != null)
            return true
        return false
    }

    fun getFavorites(): Cursor? {
        val sharedPreferencesAmplifted = SharedPreferencesAmplifted(context)
        val sort = when (sharedPreferencesAmplifted.getSortBy())  {
            2 -> "$TITLE_COL Desc"
            else -> TITLE_COL
        }
        val db = this.readableDatabase
        val query = "SELECT * FROM Favorites ORDER BY $sort"
        return db.rawQuery(query,null)
    }

    fun removeFromFavorites(name:String) {
        val db = this.writableDatabase
        val query = "DELETE FROM Favorites WHERE $TITLE_COL= '$name'"
        db.execSQL(query)
    }
}