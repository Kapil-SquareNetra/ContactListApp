package com.example.contactlistapp.databaseHelpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.tables.*

val DB_NAME: String = "Contacts.db"



class UsersHelper(
    context: Context,
    name: String = DB_NAME,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = 1
) : SQLiteOpenHelper(context, name, null, 1) {



  /* companion object {
       var myInstance: UsersHelper?= null

        fun getDBInstance(context: Context): UsersHelper {
            if (myInstance == null) {
                myInstance= UsersHelper(context)
            }
            return myInstance as UsersHelper
        }
    }*/






    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {

            UserTable(this).onCreate(db)
            ContactsTable(this).onCreate(db)
            NumbersTable(this).onCreate(db)
            EmailsTable(this).onCreate(db)
            AddressTable(this).onCreate(db)
            IMTable(this).onCreate(db)
            IMPDatesTable(this).onCreate(db)
            CircleTable(this).onCreate(db)
            CandidateTable(this).onCreate(db)
            QuestionsTable(this).onCreate(db)
            //db.close()

/*
            db.execSQL(
                "CREATE TABLE $TABLE_NAME ($USER_USERID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$USER_USERNAME TEXT NOT NULL, $USER_EMAIL TEXT UNIQUE NOT NULL, $USER_PASSWORD TEXT )"
            )*/


/*
            db.execSQL(
                "CREATE TABLE $CON_TABLE_NAME ($CON_CONTACTID INTEGER PRIMARY KEY DEFAULT 0, " +
                        "$CON_USERID INTEGER NOT NULL, $CON_CONTACTNAME TEXT NOT NULL, $CON_NICKNAME TEXT, " +
                        "$CON_PHONETICNAME TEXT,$CON_COMPANY TEXT,$CON_TITLE, $CON_WEBSITE TEXT, $CON_NOTES TEXT, $CON_GROUP TEXT )"
            )*/


           /* db.execSQL(
                "CREATE TABLE $CON_NUM_TABLE_NAME ($NUM_NUMBERID INTEGER PRIMARY KEY AUTOINCREMENT, $NUM_CONTACTID INTEGER NOT NULL, " +
                        "$NUM_TYPE TEXT NOT NULL, $NUM_NUMBER TEXT NOT NULL)"
            )*/


            /*db.execSQL(
                "CREATE TABLE $CON_EMAIL_TABLE_NAME ($EMAIL_EMAILID INTEGER PRIMARY KEY AUTOINCREMENT, $EMAIL_CONTACTID INTEGER NOT NULL, " +
                        "$EMAIL_TYPE TEXT NOT NULL, $EMAIL_EMAILADDR TEXT NOT NULL)"
            )*/


            /*db.execSQL(
                "CREATE TABLE $CON_ADDRESS_TABLE_NAME ($ADDRESS_ADDRESSID INTEGER PRIMARY KEY AUTOINCREMENT, $ADDRESS_CONTACTID INTEGER NOT NULL, " +
                        "$ADDRESS_TYPE TEXT NOT NULL, $ADDRESS_ADDR TEXT NOT NULL)"
            )*/


           /* db.execSQL(
                "CREATE TABLE $CON_IM_TABLE_NAME ($IM_ADDRESSID INTEGER PRIMARY KEY AUTOINCREMENT, $IM_CONTACTID INTEGER NOT NULL, " +
                        "$IM_TYPE TEXT NOT NULL, $IM_ADDR TEXT NOT NULL)"
            )*/


           /* db.execSQL(
                "CREATE TABLE $CON_IMPDATES_TABLE_NAME ($IMPDATES_IMPDATESID INTEGER PRIMARY KEY AUTOINCREMENT, $IMPDATES_CONTACTID INTEGER NOT NULL, " +
                        "$IMPDATES_TYPE TEXT NOT NULL, $IMPDATES_DATE TEXT NOT NULL)"
            )*/


            /*db.execSQL(
                "CREATE TABLE $CON_CIRCLE_TABLE_NAME ($CIRCLE_IMPDATESID INTEGER PRIMARY KEY AUTOINCREMENT, $CIRCLE_CONTACTID INTEGER NOT NULL, " +
                        "$CIRCLE_TYPE TEXT NOT NULL, $CIRCLE_RELATION TEXT NOT NULL)"
            )*/

        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CON_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CON_NUM_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CON_EMAIL_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CON_ADDRESS_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CON_IM_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CON_IMPDATES_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CON_CIRCLE_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $CAN_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $QUES_TABLENAME")
            onCreate(db)
        }
    }


}