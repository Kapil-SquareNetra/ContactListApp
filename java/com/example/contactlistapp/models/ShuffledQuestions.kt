package com.example.contactlistapp.models

data class ShuffledQuestions(
    val question: String,
    val correctAnswer: String,
    val answerOptionsArray: ArrayList<String>,
    val wrongAnswers: ArrayList<String>,
    var candidateSelection: String?= null
)

object Shuffled{

    var questionList: MutableList<ShuffledQuestions> = mutableListOf()
}