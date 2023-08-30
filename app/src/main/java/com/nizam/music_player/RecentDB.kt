package com.nizam.music_player

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

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
        val query = "create table if not exists $TABLE_NAME ($ID_COL text, $TITLE_COL text, $ALBUM_COL text, $ARTIST_COL text, $DURATION_COL Integer, $PATH_COL text, $ART_URI_COL text, $TIME_COL text )"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(p0)
    }


}