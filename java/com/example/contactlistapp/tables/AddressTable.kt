package com.example.contactlistapp.tables

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.AddressDetails

val CON_ADDRESS_TABLE_NAME: String = "ContactAddress"
val ADDRESS_ADDRESSID: String = "AddressID"
val ADDRESS_CONTACTID: String = "ContactID"
val ADDRESS_TYPE: String = "Type"
val ADDRESS_ADDR: String = "Addr"

class AddressTable(DB : UsersHelper): DbInterface {
    var mDb: SQLiteOpenHelper=DB
    fun insertAddressData(contactID: Int, addressType: String, addr: String): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(ADDRESS_CONTACTID,contactID)
        contentValues.put(ADDRESS_TYPE,addressType)
        contentValues.put(ADDRESS_ADDR,addr)
        var confirmation= db.insert(CON_ADDRESS_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun getAddressData(contactID: Int): MutableList<AddressDetails>? {
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $ADDRESS_CONTACTID, $ADDRESS_TYPE, $ADDRESS_ADDR " +
                " FROM $CON_ADDRESS_TABLE_NAME " +
                " WHERE $CON_CONTACTID=$contactID"
        //" AND $CON_USERID= $userID "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<AddressDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(AddressDetails(result.getString(0),result.getString(1),result.getString(2)))
        }
        db.close()
        return list

    }

    fun deleteContactData(){
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        db.delete(CON_ADDRESS_TABLE_NAME,null,null)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CON_ADDRESS_TABLE_NAME ($ADDRESS_ADDRESSID INTEGER PRIMARY KEY AUTOINCREMENT, $ADDRESS_CONTACTID INTEGER NOT NULL, " +
                    "$ADDRESS_TYPE TEXT NOT NULL, $ADDRESS_ADDR TEXT NOT NULL)"
        )
        //db.close()
    }

}