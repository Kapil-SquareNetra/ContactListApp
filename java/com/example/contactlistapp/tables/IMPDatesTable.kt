package com.example.contactlistapp.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.IMPDatesDetails

val CON_IMPDATES_TABLE_NAME: String = "ContactImpDates"
val IMPDATES_IMPDATESID: String = "ImpDatesID"
val IMPDATES_CONTACTID: String = "ContactID"
val IMPDATES_TYPE: String = "Type"
val IMPDATES_DATE: String = "Date"

class IMPDatesTable(DB : UsersHelper): DbInterface {
    var mDb: SQLiteOpenHelper=DB

    fun insertImpDatesData(contactID: Int, impDateType: String, impDate: String): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        val contentValues= ContentValues()
        contentValues.put(IMPDATES_CONTACTID,contactID)
        contentValues.put(IMPDATES_TYPE,impDateType)
        contentValues.put(IMPDATES_DATE,impDate)
        var confirmation= db.insert(CON_IMPDATES_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun getIMPDatesData(contactID: Int): MutableList<IMPDatesDetails>? {
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $IMPDATES_CONTACTID, $IMPDATES_TYPE, $IMPDATES_DATE " +
                " FROM $CON_IMPDATES_TABLE_NAME " +
                " WHERE $CON_CONTACTID=$contactID"
        //" AND $CON_USERID= $userID "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<IMPDatesDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(IMPDatesDetails(result.getString(0),result.getString(1),result.getString(2)))
        }
        db.close()
        return list

    }

    fun deleteContactData(){
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        db.delete(CON_IMPDATES_TABLE_NAME,null,null)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CON_IMPDATES_TABLE_NAME ($IMPDATES_IMPDATESID INTEGER PRIMARY KEY AUTOINCREMENT, $IMPDATES_CONTACTID INTEGER NOT NULL, " +
                    "$IMPDATES_TYPE TEXT NOT NULL, $IMPDATES_DATE TEXT NOT NULL)"
        )
        //db.close()
    }
}