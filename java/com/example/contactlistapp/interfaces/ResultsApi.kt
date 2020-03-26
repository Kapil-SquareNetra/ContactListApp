package com.example.contactlistapp.interfaces

import com.example.contactlistapp.models.ResponseAPI
import retrofit2.Call
import retrofit2.http.GET

interface ResultsApi {

    @GET("api.php?amount=20&category=18&difficulty=easy&type=multiple")
    fun getQuestions(): Call<ResponseAPI>

}