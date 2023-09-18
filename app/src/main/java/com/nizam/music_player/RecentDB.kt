package com.nizam.music_player

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import java.io.File

class RecentDB(context: Context, factory: CursorFactory?): SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "RecentSongs"
        private const val DATABASE_VERSION  = 1

        private const val TABLE_NAME = "Recent"

        const val ID_COL = "id"
        const val TITLE_COL = "title"
        const val ALBUM_COL = "album"
        const val ARTIST_COL = "artist"
        const val DURATION_COL = "duration"
        const val PATH_COL = "path"
        const val ART_URI_COL = "artUri"
        const val TIME_COL = "time"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = "create table if not exists $TABLE_NAME ($ID_COL text, $TITLE_COL text, $ALBUM_COL text, $ARTIST_COL text, $DURATION_COL Integer, $PATH_COL text, $ART_URI_COL text, $TIME_COL text)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(p0)
    }

    fun addToRecent(songData: SongsData,time: String) {

        if(songExist(songData.title)) {
            updateTime(songData.title,time)
        } else {

            val values = ContentValues()

            values.put(ID_COL,songData.id.toString())
            values.put(TITLE_COL,songData.title)
            values.put(ALBUM_COL,songData.album)
            values.put(ARTIST_COL,songData.artist)
            values.put(DURATION_COL,songData.duration)
            values.put(PATH_COL,songData.path)
            values.put(ART_URI_COL,songData.artUri)
            values.put(TIME_COL,time)

            val db = this.writableDatabase
            db.insert(TABLE_NAME,null,values)
            db.close()
        }
    }

    @SuppressLint("Range")
    fun songExist(songName: String): Boolean {

        val db = this.readableDatabase
        val query = "select $ID_COL from $TABLE_NAME where $TITLE_COL = '$songName'"
        val cursor = db.rawQuery(query,null)
        var value: String? = null

        if(cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndex(ID_COL))
        }
        cursor.close()
        if(value != null) {
            return true
        }
        return false
    }

    private fun updateTime(songName: String,time: String) {
        val db = this.writableDatabase
        val query = "update $TABLE_NAME set $TIME_COL='$time' where $TITLE_COL='$songName'"
        db.execSQL(query)
        db.close()
    }

    fun clearRecent() {
        val db = this.writableDatabase
        db.execSQL("delete from $TABLE_NAME")
        db.close()
    }

    @SuppressLint("Range")
    fun getAllSongs(): ArrayList<SongsData> {
        val query = "select * from $TABLE_NAME order by $TIME_COL DESC"
        val cursor = this.readableDatabase.rawQuery(query,null)
        println("query : $query ")
        val songsList: ArrayList<SongsData> = ArrayList()

        if(cursor.moveToFirst()) {
            do {
                val idC = cursor.getString(cursor.getColumnIndex(ID_COL))
                val titleC = cursor.getString(cursor.getColumnIndex(TITLE_COL))
                val albumC = cursor.getString(cursor.getColumnIndex(ALBUM_COL))
                val artistC = cursor.getString(cursor.getColumnIndex(ARTIST_COL))
                val durationC = cursor.getLong(cursor.getColumnIndex(DURATION_COL))
                val pathC = cursor.getString(cursor.getColumnIndex(PATH_COL))
                val artUriC = cursor.getString(cursor.getColumnIndex(ART_URI_COL))

                val music = SongsData(
                    id = Uri.parse(idC),
                    title = titleC,
                    album = albumC,
                    duration = durationC,
                    path = pathC,
                    artist = artistC,
                    artUri = artUriC,
                    dateModified = null
                )
                val file = File(pathC)
                if (file.exists()) {
                    songsList.add(music)
                }
            } while(cursor.moveToNext())
        }
        cursor.close()
        return songsList
    }

}