package com.example.contactlistapp.workerClass

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.models.CandidateDetails
import com.example.contactlistapp.models.CandidateList
import com.example.contactlistapp.models.CandidateResponses
import com.example.contactlistapp.models.candidateSupplier
import com.example.contactlistapp.tables.CandidateTable
import com.example.contactlistapp.tables.QuestionsTable
import java.lang.Exception

class CandidateImportWorker(context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {

    val con= context


    override fun doWork(): Result {
        getCandidateData()
        return Result.success()
    }

    fun getCandidateData(){
        var usersHelper= UsersHelper(con)
        var mCandidateTable= CandidateTable(usersHelper)
        var mQuestionsTable= QuestionsTable(usersHelper)


        try {
           val list: MutableList<CandidateDetails>? = mCandidateTable.getAllCandidates()
            candidateSupplier.candidateItems.clear()
            list?.let {
                for(i in it){
                    val candidateID= i.candidateID
                    val candidateName= i.candidateName
                    val candidateEmail= i.candidateEmail
                    val candidatePhone= i.candidatePhone
                    val candidateQuestionResponse: MutableList<CandidateResponses> =
                        mutableListOf()
                    val responseList=  mQuestionsTable.getAllQuestionsData(candidateID.toInt())
                    responseList?.let {

                        for (j in it){
                            candidateQuestionResponse.add(CandidateResponses(j.question,j.correctAnswer,j.candidateSelection))
                        }
                    }
                    candidateSupplier.candidateItems.add(CandidateList(candidateName,candidateEmail,candidatePhone,candidateQuestionResponse))

                }
            }

        }
        catch (e: Exception){
            println(e.printStackTrace())
        }
    }


}