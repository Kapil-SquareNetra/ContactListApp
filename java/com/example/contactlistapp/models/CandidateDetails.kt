package com.example.contactlistapp.models

data class CandidateList(
    val candidateName: String,
    val candidateEmail: String,
    val candidateNumber: String,
    val answerList: MutableList<CandidateResponses>?=null
)

data class CandidateResponses(
    val question: String,
    val correctAnswer: String,
    val candidateResponse: String?=null
)

object candidateSupplier{

    var candidateItems: MutableList<CandidateList> = mutableListOf()
    var candidateDisplayItems: MutableList<CandidateList> = mutableListOf()
}

