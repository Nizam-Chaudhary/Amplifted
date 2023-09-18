package com.nizam.music_player

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import java.io.File

class PlaylistsDB(val context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

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
        const val DATE_MODIFIED_COL = "dateAdded"
    }


    //this function creates the table for playlist if it does not exists.
    override fun onCreate(p0: SQLiteDatabase?) {
        var query =
            "Create Table If Not Exists $PLAYLIST_MASTER($PLAYLIST_NAME_COL TEXT Primary Key)"
        p0?.execSQL(query)
        query =
            "CREATE TABLE IF NOT EXISTS $PLAYLIST($PLAYLIST_NAME_COL TEXT,$ID_COL TEXT, $TITLE_COL TEXT, $ALBUM_COL TEXT, $ARTIST_COL TEXT, $DURATION_COL INTEGER, $PATH_COL TEXT, $ART_URI_COL TEXT, $DATE_MODIFIED_COL Text)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $PLAYLIST_MASTER")
        p0?.execSQL("DROP TABLE IF EXISTS $PLAYLIST")
        onCreate(p0)
    }

    fun createPlayList(playListName: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PLAYLIST_NAME_COL, playListName)
        db.insert(PLAYLIST_MASTER, null, values)
    }

    //this function is used to add the song to the playlist
    fun addToPlaylist(songsData: SongsData, playListName: String) {

        if (!songExists(songsData.title, playListName)) {
            val values = ContentValues()

            values.put(PLAYLIST_NAME_COL, playListName)
            values.put(ID_COL, songsData.id.toString())
            values.put(TITLE_COL, songsData.title)
            values.put(ALBUM_COL, songsData.album)
            values.put(ARTIST_COL, songsData.artist)
            values.put(DURATION_COL, songsData.duration)
            values.put(PATH_COL, songsData.path)
            values.put(ART_URI_COL, songsData.artUri)
            values.put(DATE_MODIFIED_COL, songsData.dateModified)

            val db = this.writableDatabase
            db.insert(PLAYLIST, null, values)

            db.close()
        }
    }

    fun removeFromPlayList(playListName: String, songName: String) {
        val db = this.writableDatabase
        val query =
            "DELETE FROM $PLAYLIST WHERE $TITLE_COL= '$songName' And $PLAYLIST_NAME_COL = '$playListName'"
        db.execSQL(query)
    }

    fun removePlaylist(playlistName: String) {
        val db = this.writableDatabase
        var query = "Delete From $PLAYLIST_MASTER where $PLAYLIST_NAME_COL = '$playlistName' "
        db.execSQL(query)
        query = "Delete From $PLAYLIST where $PLAYLIST_NAME_COL = '$playlistName'"
        db.execSQL(query)
    }

    private fun songExists(name: String, playlistName: String): Boolean {
        val db = this.readableDatabase

        val query =
            "SELECT $ID_COL FROM $PLAYLIST WHERE $TITLE_COL = '$name' and $PLAYLIST_NAME_COL='$playlistName'"
        val cursor = db.rawQuery(query, null)
        var value: String? = null
        if (cursor.moveToFirst())
            value = cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDB.ID_COL))
        cursor.close()
        if (value != null)
            return true
        return false
    }

    fun getPlayListNames(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT $PLAYLIST_NAME_COL FROM $PLAYLIST_MASTER"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val playlistNameCol = cursor.getColumnIndexOrThrow(PLAYLIST_NAME_COL)
            do {
                list.add(cursor.getString(playlistNameCol))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getPlayListArtUri(playListName: String): String? {
        var artUri: String? = null
        val db = this.readableDatabase
        val query =
            "Select $ART_URI_COL from $PLAYLIST where $PLAYLIST_NAME_COL = '$playListName' Order by $TITLE_COL Limit 1"
        val cursor = db.rawQuery(query, null)
        val artUriColumn = cursor.getColumnIndexOrThrow(ART_URI_COL)
        if (cursor.moveToFirst()) {
            artUri = cursor.getString(artUriColumn)
        }
        cursor.close()
        return artUri
    }

    fun getPlayListSongs(playListName: String): ArrayList<SongsData> {

        val sharedPreferencesAmplifted = SharedPreferencesAmplifted(context)
        val sort = when (sharedPreferencesAmplifted.getSortBy())  {
            2 -> "$TITLE_COL Desc"
            3 -> "$DATE_MODIFIED_COL Desc"
            4 -> DATE_MODIFIED_COL
            else -> TITLE_COL
        }

        val temp: ArrayList<SongsData> = ArrayList()
        val db = this.readableDatabase
        val query =
            "Select * from $PLAYLIST where $PLAYLIST_NAME_COL = '$playListName' order by $sort"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val idColumn = cursor.getColumnIndexOrThrow(ID_COL)
            val titleColumn = cursor.getColumnIndexOrThrow(TITLE_COL)
            val albumColumn = cursor.getColumnIndexOrThrow(ALBUM_COL)
            val artistColumn = cursor.getColumnIndexOrThrow(ARTIST_COL)
            val durationColumn = cursor.getColumnIndexOrThrow(DURATION_COL)
            val pathColumn = cursor.getColumnIndexOrThrow(PATH_COL)
            val artUriColumn = cursor.getColumnIndexOrThrow(ART_URI_COL)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(DATE_MODIFIED_COL)
            do {
                val idC = cursor.getString(idColumn)
                val titleC = cursor.getString(titleColumn)
                val albumC = cursor.getString(albumColumn)
                val artistC = cursor.getString(artistColumn)
                val durationC = cursor.getLong(durationColumn)
                val pathC = cursor.getString(pathColumn)
                val artUriC = cursor.getString(artUriColumn)
                val dateAddedC = cursor.getString(dateAddedColumn)

                val music = SongsData(
                    id = Uri.parse(idC),
                    title = titleC,
                    album = albumC,
                    duration = durationC,
                    path = pathC,
                    artist = artistC,
                    artUri = artUriC,
                    dateModified = dateAddedC
                )
                val file = File(pathC)
                if (file.exists()) {
                    temp.add(music)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return temp
    }

    fun getPlaylistSongData(songName: String, playlistName: String): SongsData {
        val db = this.readableDatabase
        val query =
            "select * from $PLAYLIST where $TITLE_COL='$songName' and $PLAYLIST_NAME_COL='$playlistName'"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val idC = cursor.getString(cursor.getColumnIndexOrThrow(ID_COL))
        val titleC = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
        val albumC = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_COL))
        val artistC = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COL))
        val durationC = cursor.getLong(cursor.getColumnIndexOrThrow(DURATION_COL))
        val pathC = cursor.getString(cursor.getColumnIndexOrThrow(PATH_COL))
        val artUriC = cursor.getString(cursor.getColumnIndexOrThrow(ART_URI_COL))
        val dateAddedC = cursor.getString(cursor.getColumnIndexOrThrow(DATE_MODIFIED_COL))

        cursor.close()
        return SongsData(
            id = Uri.parse(idC),
            title = titleC,
            album = albumC,
            artist = artistC,
            duration = durationC,
            path = pathC,
            artUri = artUriC,
            dateModified = dateAddedC
        )
    }
}