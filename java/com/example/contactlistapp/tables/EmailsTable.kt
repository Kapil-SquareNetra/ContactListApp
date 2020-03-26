package com.example.contactlistapp.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.ContactDetails
import com.example.contactlistapp.models.EmailDetails

val CON_EMAIL_TABLE_NAME: String = "ContactEmail"
val EMAIL_EMAILID: String = "EmailID"
val EMAIL_CONTACTID: String = "ContactID"
val EMAIL_TYPE: String = "Type"
val EMAIL_EMAILADDR: String = "EmailAddr"

class EmailsTable (DB : UsersHelper): DbInterface{
    var mDb: SQLiteOpenHelper=DB

    fun insertEmailData(contactID: Int, emailType: String, emailAddr: String): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(EMAIL_CONTACTID,contactID)
        contentValues.put(EMAIL_TYPE,emailType)
        contentValues.put(EMAIL_EMAILADDR,emailAddr)
        var confirmation= db.insert(CON_EMAIL_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun getEmailData(contactID: Int): MutableList<EmailDetails>? {
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $EMAIL_CONTACTID, $EMAIL_TYPE, $EMAIL_EMAILADDR " +
                " FROM $CON_EMAIL_TABLE_NAME" +
                " WHERE $CON_CONTACTID=$contactID"
        //   " AND $CON_USERID= $userID "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<EmailDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(EmailDetails(result.getString(0),result.getString(1),result.getString(2)))
        }
        db.close()
        return list

    }

    fun deleteContactData(){
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        db.delete(CON_EMAIL_TABLE_NAME,null,null)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CON_EMAIL_TABLE_NAME ($EMAIL_EMAILID INTEGER PRIMARY KEY AUTOINCREMENT, $EMAIL_CONTACTID INTEGER NOT NULL, " +
                    "$EMAIL_TYPE TEXT NOT NULL, $EMAIL_EMAILADDR TEXT NOT NULL)"
        )
        //db.close()
    }
}