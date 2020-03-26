package com.example.contactlistapp.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.ContactDetails

val CON_TABLE_NAME: String = "Contacts"
val CON_CONTACTID: String = "ContactID"
val CON_USERID: String = "UserID"
val CON_CONTACTNAME: String = "ContactName"
val CON_NICKNAME: String = "NickName"
val CON_PHONETICNAME: String = "PhoneticName"
val CON_COMPANY: String = "Company"
val CON_TITLE: String = "Title"
val CON_WEBSITE: String = "Website"
val CON_NOTES: String = "Notes"
val CON_GROUP: String = "Groups"
val CON_DP: String="DisplayPicture"

class ContactsTable(DB : UsersHelper) : DbInterface{

    var mDb: SQLiteOpenHelper=DB
    var sDb= mDb

    fun getContactsID(): MutableList<String>? {
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT IFNULL(MAX($CON_CONTACTID),0) AS ID FROM $CON_TABLE_NAME "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<String>? = mutableListOf()
        while (result.moveToNext())
        list?.add(result.getString(0))?:"0"

        db.close()
        return list

    }

    //insert data

    fun insertContactData(contactID: Int, userID: Int, contactName: String, nickName: String?= null,
                          phoneticName: String?= null, companyName:String?=null, title: String?=null,
                          website: String?=null, notes: String?=null, group: String?=null): Boolean{
       // , displayImage: String?=null
       // val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(CON_CONTACTID,contactID)
        contentValues.put(CON_USERID,userID)
        contentValues.put(CON_CONTACTNAME,contactName)
        contentValues.put(CON_NICKNAME,nickName)
        contentValues.put(CON_PHONETICNAME,phoneticName)
        contentValues.put(CON_COMPANY,companyName)
        contentValues.put(CON_TITLE,title)
        contentValues.put(CON_WEBSITE,website)
        contentValues.put(CON_NOTES,notes)
        contentValues.put(CON_GROUP,group)
        //contentValues.put(CON_DP,displayImage )
        var confirmation= db.insert(CON_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true

    }


    fun getContactData(contactID: Int?=null, userID: Int, userName: String?=null): MutableList<ContactDetails>?{
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $CON_CONTACTID, $CON_CONTACTNAME, $CON_NICKNAME, " +
                " $CON_PHONETICNAME, $CON_COMPANY, $CON_TITLE, $CON_WEBSITE, $CON_NOTES, $CON_GROUP " +
                " FROM $CON_TABLE_NAME " +
                " WHERE $CON_USERID= $userID "
        if (contactID!=null)
            sSQL+=" AND $CON_CONTACTID=$contactID"

        if(userName!=null)
            sSQL+=" AND $CON_CONTACTNAME='$userName'"

        sSQL+=" ORDER BY $CON_CONTACTNAME"

        var result: Cursor= db.rawQuery(sSQL,null)
        var list: MutableList<ContactDetails>? = mutableListOf()
        while (result.moveToNext()){
           list?.add(ContactDetails(result.getString(0),result.getString(1),result.getString(2),
               result.getString(3),result.getString(4),result.getString(5),result.getString(6),
               result.getString(7),
               result.getString(8)))
        }
        db.close()
        return list

    }

    fun deleteContactData(){
       // val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        db.delete(CON_TABLE_NAME,null,null)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CON_TABLE_NAME ($CON_CONTACTID INTEGER PRIMARY KEY DEFAULT 0, " +
                    "$CON_USERID INTEGER NOT NULL, $CON_CONTACTNAME TEXT NOT NULL, $CON_NICKNAME TEXT, " +
                    "$CON_PHONETICNAME TEXT,$CON_COMPANY TEXT,$CON_TITLE, $CON_WEBSITE TEXT, $CON_NOTES TEXT, $CON_GROUP TEXT )"
        )
        //db.close()
    }


}