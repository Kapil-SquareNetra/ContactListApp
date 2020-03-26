package com.example.contactlistapp.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.IMDetails


val CON_IM_TABLE_NAME: String = "ContactIM"
val IM_ADDRESSID: String = "IMID"
val IM_CONTACTID: String = "ContactID"
val IM_TYPE: String = "Type"
val IM_ADDR: String = "IMAddr"

class IMTable(DB : UsersHelper):DbInterface {
    var mDb: SQLiteOpenHelper=DB

    fun insertIMData(contactID: Int, iMType: String, iMAddress: String): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(IM_CONTACTID,contactID)
        contentValues.put(IM_TYPE,iMType)
        contentValues.put(IM_ADDR,iMAddress)
        var confirmation= db.insert(CON_IM_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun getIMData(contactID: Int): MutableList<IMDetails>? {
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $IM_CONTACTID, $IM_TYPE, $IM_ADDR " +
                " FROM $CON_IM_TABLE_NAME " +
                " WHERE $CON_CONTACTID=$contactID"
        //" AND $CON_USERID= $userID "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<IMDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(IMDetails(result.getString(0),result.getString(1),result.getString(2)))
        }
        db.close()
        return list

    }

    fun deleteContactData(){
       // val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        db.delete(CON_IM_TABLE_NAME,null,null)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CON_IM_TABLE_NAME ($IM_ADDRESSID INTEGER PRIMARY KEY AUTOINCREMENT, $IM_CONTACTID INTEGER NOT NULL, " +
                    "$IM_TYPE TEXT NOT NULL, $IM_ADDR TEXT NOT NULL)"
        )
        //db.close()
    }
}