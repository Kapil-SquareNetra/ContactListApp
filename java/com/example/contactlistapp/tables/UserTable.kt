package com.example.contactlistapp.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.UserDetails

val TABLE_NAME: String = "Users"
val USER_USERID: String = "UserID"
val USER_USERNAME: String = "UserName"
val USER_EMAIL: String = "Email"
val USER_PASSWORD: String = "Password"

class UserTable(DB : UsersHelper) : DbInterface {
    var mDb: SQLiteOpenHelper= DB

    fun insertUserData(userName: String, email: String, password: String?=null): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(USER_USERNAME,userName)
        contentValues.put(USER_EMAIL,email)
        contentValues.put(USER_PASSWORD,password)
        var confirmation= db.insert(TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun findUserData(userName:String, email:String?=null, password: String?=null): MutableList<UserDetails>? {
       // val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        var sSQL= "SELECT $USER_USERNAME, $USER_EMAIL, $USER_PASSWORD, $USER_USERID FROM  $TABLE_NAME WHERE $USER_USERNAME= '$userName' "
        if (!email.isNullOrEmpty()) sSQL+=" AND $USER_EMAIL= '$email' "
        if(!password.isNullOrEmpty()) sSQL+=" AND $USER_PASSWORD='$password' "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<UserDetails>?= mutableListOf()
        while (result.moveToNext()){
            list?.add(UserDetails(result.getString(0),result.getString(1),
                result.getString(2),result.getString(3)))
        }
        db.close()
        return list


    }

    override fun onCreate(db: SQLiteDatabase) {

            db.execSQL(
                "CREATE TABLE $TABLE_NAME ($USER_USERID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$USER_USERNAME TEXT NOT NULL, $USER_EMAIL TEXT UNIQUE NOT NULL, $USER_PASSWORD TEXT )"
            )
        //db.close()
    }


}