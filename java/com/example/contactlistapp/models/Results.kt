package com.example.contactlistapp.models

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("question")
   var question: String,
    @SerializedName("correct_answer")
   var correctAnswer: String,
    @SerializedName("incorrect_answers")
   var incorrectAnswer: ArrayList<String>
)