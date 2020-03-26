package com.example.contactlistapp.models

import com.google.gson.annotations.SerializedName

data class ResponseAPI(
   @SerializedName("results")
   var results: MutableList<Results>
)

object QuestionsObject{

   var questionList: MutableList<Results> = mutableListOf()
}