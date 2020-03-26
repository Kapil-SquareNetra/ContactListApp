package com.example.contactlistapp.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.NumberDetails

val CON_NUM_TABLE_NAME: String = "ContactNumbers"
val NUM_NUMBERID: String = "NumberID"
val NUM_CONTACTID: String = "ContactID"
val NUM_TYPE: String = "Type"
val NUM_NUMBER: String = "Number"

class NumbersTable(DB : UsersHelper) : DbInterface {
    //val mDb : SQLiteOpenHelper = DB
    var mDb: SQLiteOpenHelper=DB
    fun insertNumberData(contactID: Int, numType: String, number: String): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(NUM_CONTACTID,contactID)
        contentValues.put(NUM_TYPE,numType)
        contentValues.put(NUM_NUMBER,number)
        var confirmation= db.insert(CON_NUM_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun getNumberData(contactID: Int): MutableList<NumberDetails>? {
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $NUM_CONTACTID, $NUM_TYPE, $NUM_NUMBER " +
                " FROM $CON_NUM_TABLE_NAME " +
                " WHERE $CON_CONTACTID=$contactID"
        //" AND $CON_USERID= $userID "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<NumberDetails>? = mutableListOf()
        while(result.moveToNext()){
            list?.add(NumberDetails(result.getString(0), result.getString(1), result.getString(2)))
        }
        db.close()
        return list

    }


    fun deleteContactData(){
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        db.delete(CON_NUM_TABLE_NAME,null,null)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CON_NUM_TABLE_NAME ($NUM_NUMBERID INTEGER PRIMARY KEY AUTOINCREMENT, $NUM_CONTACTID INTEGER NOT NULL, " +
                    "$NUM_TYPE TEXT NOT NULL, $NUM_NUMBER TEXT NOT NULL)"
        )
        //db.close()
    }


}