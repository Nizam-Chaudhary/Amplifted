package com.nizam.music_player

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

class PlaylistsDB(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION) {

        companion object {
            const val DATABASE_NAME = "Playlists"
            const val DATABASE_VERSION = 1
            const val PLAYLIST_MASTER = "PlayListMaster"
            const val PLAYLIST = "PlayList"

            const val PLAYLIST_NAME_COL = "playListName"
            const val ID_COL = "id"
            const val TITLE_COL = "title"
            const val ALBUM_COL = "album"
            const val ARTIST_COL = "artist"
            const val DURATION_COL = "duration"
            const val PATH_COL = "path"
            const val ART_URI_COL = "artUri"
        }


    //this function creates the table for playlist if it does not exists.
    override fun onCreate(p0: SQLiteDatabase?) {
        var query = "Create Table If Not Exists $PLAYLIST_MASTER($PLAYLIST_NAME_COL TEXT Primary Key)"
        p0?.execSQL(query)
        query = "CREATE TABLE IF NOT EXISTS $PLAYLIST($PLAYLIST_NAME_COL TEXT,$ID_COL TEXT PRIMARY KEY, $TITLE_COL TEXT, $ALBUM_COL TEXT, $ARTIST_COL TEXT, $DURATION_COL INTEGER, $PATH_COL TEXT, $ART_URI_COL TEXT)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $PLAYLIST_MASTER")
        p0?.execSQL("DROP TABLE IF EXISTS $PLAYLIST")
        onCreate(p0)
    }

    fun createPlayList(playListName:String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PLAYLIST_NAME_COL,playListName)
        db.insert(PLAYLIST_MASTER,null,values)
    }

    //this function is used to add the song to the playlist
    fun addToPlaylist(songsData: SongsData,playListName:String) {

        val values = ContentValues()

        values.put(PLAYLIST_NAME_COL,playListName)
        values.put(ID_COL,songsData.id)
        values.put(TITLE_COL,songsData.title)
        values.put(ALBUM_COL,songsData.album)
        values.put(ARTIST_COL,songsData.artist)
        values.put(DURATION_COL,songsData.duration)
        values.put(PATH_COL,songsData.path)
        values.put(ART_URI_COL,songsData.artUri)

        val db = this.writableDatabase
        db.insert(PLAYLIST,null,values)

        db.close()
    }

    fun removeFromPlayList(playListName: String,songName:String) {
        val db = this.writableDatabase
        val query = "DELETE FROM $PLAYLIST WHERE $TITLE_COL= '$songName' And $PLAYLIST_NAME_COL = '$playListName'"
        db.execSQL(query)
    }

    fun removePlaylist(playlistName: String) {
        val db=this.writableDatabase
        var query = "Delete From $PLAYLIST_MASTER where $PLAYLIST_NAME_COL = '$playlistName' "
        db.execSQL(query)
        query = "Delete From $PLAYLIST where $PLAYLIST_NAME_COL = '$playlistName'"
        db.execSQL(query)
    }

    @SuppressLint("Range")
    fun songExists(name: String): Boolean {
        val db = this.readableDatabase

        val query = "SELECT $ID_COL FROM $PLAYLIST WHERE $TITLE_COL = '$name'"
        val cursor = db.rawQuery(query,null)
        var value: String? = null
        if(cursor.moveToFirst())
            value = cursor.getString(cursor.getColumnIndex(FavoritesDB.ID_COL))
        cursor.close()
        if(value != null)
            return true
        return false
    }

    @SuppressLint("Range")
    fun getPlayListNames(): ArrayList<String> {
        val list:ArrayList<String> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT $PLAYLIST_NAME_COL FROM $PLAYLIST_MASTER"
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(PLAYLIST_NAME_COL)))
            }while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    @SuppressLint("Range")
    fun getPlayListArtUri(playListName: String): String? {
        var artUri:String? = null
        val db = this.readableDatabase
        val query = "Select $ART_URI_COL from $PLAYLIST where $PLAYLIST_NAME_COL = '$playListName' Order by $TITLE_COL Limit 1"
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()) {
            artUri = cursor.getString(cursor.getColumnIndex(ART_URI_COL))
        }
        cursor.close()
        return artUri
    }

    @SuppressLint("Range")
    fun getPlayListSongs(playListName: String):ArrayList<SongsData> {
        val temp: ArrayList<SongsData> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $PLAYLIST where $PLAYLIST_NAME_COL = '$playListName'"
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()) {
            do {
                val idC = cursor.getString(cursor.getColumnIndex(ID_COL))
                val titleC = cursor.getString(cursor.getColumnIndex(TITLE_COL))
                val albumC = cursor.getString(cursor.getColumnIndex(ALBUM_COL))
                val artistC = cursor.getString(cursor.getColumnIndex(ARTIST_COL))
                val durationC = cursor.getLong(cursor.getColumnIndex(DURATION_COL))
                val pathC = cursor.getString(cursor.getColumnIndex(PATH_COL))
                val artUriC = cursor.getString(cursor.getColumnIndex(ART_URI_COL))

                val music = SongsData(id = idC,title = titleC, album = albumC,duration = durationC, path = pathC, artist = artistC, artUri = artUriC)
                val file = File(pathC)
                if(file.exists()) {
                    temp.add(music)
                }
            }while (cursor.moveToNext())
        }
        cursor.close()
        return temp
    }
}