package com.example.contactlistapp.interfaces

import android.database.sqlite.SQLiteDatabase

interface DbInterface {
    fun onCreate(db: SQLiteDatabase)
}