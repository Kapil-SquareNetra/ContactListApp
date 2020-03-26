package com.example.contactlistapp.tables

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.interfaces.DbInterface
import com.example.contactlistapp.models.QuestionDetails


val QUES_TABLENAME: String = "Questions"
val QUES_QUESTIONID: String = "QuestionID"
val QUES_CANDIDATEID: String = "CandidateID"
val QUES_QUESTION: String = "Question"
val QUES_CORRECTANSWER: String = "CorrectAnswer"
val QUES_WRONGANSWER1: String = "WrongAnswer1"
val QUES_WRONGANSWER2: String = "WrongAnswer2"
val QUES_WRONGANSWER3: String = "WrongAnswer3"
val QUES_CANDIDATESELECTION: String = "CandidateSelection"

class QuestionsTable(DB: UsersHelper): DbInterface {

    var mDb: SQLiteOpenHelper = DB

    fun insertQuestionData(candidateID: Int, question: String, correctAnswer: String,
                          wrongAnswer1: String, wrongAnswer2:String, wrongAnswer3: String,
                          candidateSelection: String?=null): Boolean{
        // , displayImage: String?=null
        // val db: SQLiteDatabase = mDb.writableDatabase
        var db: SQLiteDatabase= mDb.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(QUES_CANDIDATEID,candidateID)
        contentValues.put(QUES_QUESTION,question)
        contentValues.put(QUES_CORRECTANSWER,correctAnswer)
        contentValues.put(QUES_WRONGANSWER1,wrongAnswer1)
        contentValues.put(QUES_WRONGANSWER2,wrongAnswer2)
        contentValues.put(QUES_WRONGANSWER3,wrongAnswer3)
        contentValues.put(QUES_CANDIDATESELECTION,candidateSelection)
        var confirmation= db.insert(QUES_TABLENAME, null, contentValues)
        db.close()
        if (confirmation.equals(-1)) return false
        else return true

    }

    fun getAllQuestionsData(candidateID: Int): MutableList<QuestionDetails>?{
        var db: SQLiteDatabase= mDb.writableDatabase

        var sSQL=" SELECT $QUES_QUESTION, $QUES_CORRECTANSWER, " +
                " $QUES_WRONGANSWER1, $QUES_WRONGANSWER2, $QUES_WRONGANSWER3,  " +
                " $QUES_CANDIDATESELECTION " +
                " FROM $QUES_TABLENAME " +
                " WHERE $QUES_CANDIDATEID=$candidateID "

        var result: Cursor = db.rawQuery(sSQL,null)
        var list: MutableList<QuestionDetails>? = mutableListOf()
        while (result.moveToNext()){
            list?.add(
                QuestionDetails(result.getString(0),result.getString(1),
                    result.getString(2),result.getString(3),result.getString(4),
                    result.getString(5))
            )
        }
        return list
    }






    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $QUES_TABLENAME ($QUES_QUESTIONID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$QUES_CANDIDATEID INTEGER NOT NULL, $QUES_QUESTION TEXT NOT NULL, $QUES_CORRECTANSWER TEXT, " +
                    "$QUES_WRONGANSWER1 TEXT,$QUES_WRONGANSWER2 TEXT,$QUES_WRONGANSWER3, $QUES_CANDIDATESELECTION TEXT )"
        )
    }
}