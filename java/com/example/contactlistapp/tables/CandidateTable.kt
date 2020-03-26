package com.example.contactlistapp.tables

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.CandidateDetails
import com.example.contactlistapp.models.ContactDetails
import com.example.contactlistapp.models.UserDetails

val CAN_TABLE_NAME: String = "Candidate"
val CAN_CANDIDATEID: String = "CandidateID"
val CAN_CANDIDATENAME: String = "CandidateName"
val CAN_CANDIDATEEMAIL: String = "CandidateEmail"
val CAN_CANDIDATENUMBER: String = "CandidateNumber"



class CandidateTable(DB: UsersHelper): DbInterface {

    var mDb: SQLiteOpenHelper = DB

    fun insertCandidateData(candidateName: String, candidateEmail: String, candidateNumber: String): Boolean{
        //val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(CAN_CANDIDATENAME,candidateName)
        contentValues.put(CAN_CANDIDATEEMAIL,candidateEmail)
        contentValues.put(CAN_CANDIDATENUMBER,candidateNumber)
        var confirmation= db.insert(CAN_TABLE_NAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true
    }

    fun getAllCandidates(): MutableList<CandidateDetails>?{
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $CAN_CANDIDATEID, $CAN_CANDIDATENAME, $CAN_CANDIDATEEMAIL, " +
                " $CAN_CANDIDATENUMBER " +
                " FROM $CAN_TABLE_NAME "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<CandidateDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(
                CandidateDetails(result.getString(0),result.getString(1),
                    result.getString(2), result.getString(3))
            )
        }
        db.close()
    return list
    }

    fun getCurrentCandidate(name:String, email: String, phone: String): MutableList<CandidateDetails>?{
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $CAN_CANDIDATEID, $CAN_CANDIDATENAME, $CAN_CANDIDATEEMAIL, " +
                " $CAN_CANDIDATENUMBER " +
                " FROM $CAN_TABLE_NAME "
        sSQL+=" WHERE $CAN_CANDIDATENAME='$name' "
        sSQL+=" AND $CAN_CANDIDATEEMAIL='$email' "
        sSQL+=" AND $CAN_CANDIDATENUMBER='$phone' "
        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<CandidateDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(
                CandidateDetails(result.getString(0),result.getString(1),
                    result.getString(2), result.getString(3))
            )
        }
        db.close()
        return list
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $CAN_TABLE_NAME ($CAN_CANDIDATEID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$CAN_CANDIDATENAME TEXT NOT NULL, $CAN_CANDIDATEEMAIL TEXT UNIQUE NOT NULL, $CAN_CANDIDATENUMBER TEXT )"
        )
    }


}