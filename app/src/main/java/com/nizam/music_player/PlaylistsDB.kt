package com.nizam.music_player

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlaylistsDB(context: Context, factory: SQLiteDatabase.CursorFactory?, private val playlistName: String):
    SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION) {

        companion object {
            const val DATABASE_NAME = "Playlists"
            const val DATABASE_VERSION = 1
        }


    //this function creates the table for playlist if it does not exists.
    override fun onCreate(p0: SQLiteDatabase?) {
        val query = "CREATE TABLE IF NOT EXISTS $playlistName(${FavoritesDB.ID_COL} TEXT PRIMARY KEY, ${FavoritesDB.TITLE_COL} TEXT, ${FavoritesDB.ALBUM_COL} TEXT, ${FavoritesDB.ARTIST_COL} TEXT, ${FavoritesDB.DURATION_COL} INTEGER, ${FavoritesDB.PATH_COL} TEXT, ${FavoritesDB.ART_URI_COL} TEXT)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $playlistName")
        onCreate(p0)
    }

    //this function is used to add the song to the playlist
    fun addToPlaylist(songsData: SongsData) {

        val values = ContentValues()

        values.put(FavoritesDB.ID_COL,songsData.id)
        values.put(FavoritesDB.TITLE_COL,songsData.title)
        values.put(FavoritesDB.ALBUM_COL,songsData.album)
        values.put(FavoritesDB.ARTIST_COL,songsData.artist)
        values.put(FavoritesDB.DURATION_COL,songsData.duration)
        values.put(FavoritesDB.PATH_COL,songsData.path)
        values.put(FavoritesDB.ART_URI_COL,songsData.artUri)

        val db = this.writableDatabase
        db.insert(playlistName,null,values)

        db.close()
    }

    fun removeFromFavorites(name:String) {
        val db = this.writableDatabase
        val query = "DELETE FROM $playlistName WHERE ${FavoritesDB.TITLE_COL}= '$name'"
        db.execSQL(query)
    }

    fun removePlaylist(playlistName: String) {
        val db=this.writableDatabase
        val query = "DROP TABLE $playlistName"
        db.execSQL(query)
    }

    @SuppressLint("Range")
    fun songExists(name: String): Boolean {
        val db = this.readableDatabase

        val query = "SELECT ${FavoritesDB.ID_COL} FROM $playlistName WHERE ${FavoritesDB.TITLE_COL} = '$name'"
        val cursor = db.rawQuery(query,null)
        var value: String? = null
        if(cursor.moveToFirst())
            value = cursor.getString(cursor.getColumnIndex(FavoritesDB.ID_COL))
        cursor.close()
        if(value != null)
            return true
        return false
    }
}