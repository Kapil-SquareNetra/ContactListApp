package com.example.contactlistapp.tables

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.*
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.CircleDetails

val CON_CIRCLE_TABLE_NAME: String = "ContactCircle"
val CIRCLE_IMPDATESID: String = "CircleID"
val CIRCLE_CONTACTID: String = "ContactID"
val CIRCLE_TYPE: String = "Type"
val CIRCLE_RELATION: String = "Relation"

class CircleTable (DB : UsersHelper): DbInterface{
    var mDb: SQLiteOpenHelper=DB

    fun insertCircleData(contactID: Int, circleType: String, relation: String): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(CIRCLE_CONTACTID,contactID)
        contentValues.put(CIRCLE_TYPE,circleType)
        contentValues.put(CIRCLE_RELATION,relation)
        var confirmation= db.insert(CON_CIRCLE_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun getCircleData(contactID: Int): MutableList<CircleDetails>? {
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $CIRCLE_CONTACTID, $CIRCLE_TYPE, $CIRCLE_RELATION " +
                " FROM $CON_CIRCLE_TABLE_NAME " +
                " WHERE $CON_CONTACTID=$contactID"
        //" AND $CON_USERID= $userID "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<CircleDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(CircleDetails(result.getString(0),result.getString(1),result.getString(2)))
        }
        db.close()
        return list

    }

    fun deleteContactData(){
        //val db: SQLiteDatabase= mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        db.delete(CON_CIRCLE_TABLE_NAME,null,null)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CON_CIRCLE_TABLE_NAME ($CIRCLE_IMPDATESID INTEGER PRIMARY KEY AUTOINCREMENT, $CIRCLE_CONTACTID INTEGER NOT NULL, " +
                    "$CIRCLE_TYPE TEXT NOT NULL, $CIRCLE_RELATION TEXT NOT NULL)"
        )
        //db.close()
    }

}