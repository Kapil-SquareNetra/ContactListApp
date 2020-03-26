package com.example.contactlistapp.activities

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.contactlistapp.R
import com.example.contactlistapp.fragments.QuestionsFragment
import com.example.contactlistapp.interfaces.ResultsApi
import com.example.contactlistapp.models.QuestionsObject
import com.example.contactlistapp.models.ResponseAPI
import com.example.contactlistapp.models.Shuffled
import com.example.contactlistapp.models.ShuffledQuestions
import kotlinx.android.synthetic.main.activity_test.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var correctAnswers=0
var wrongAnswers=0
var unAttendedAnswer=0

@Suppress("DEPRECATION")
class QuestionnaireActivity : AppCompatActivity() {

    val manager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        progressFetchQuestionsLayout.visibility= View.VISIBLE
        fetchFromAPI()
        btnBeginTest.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Begin Test Confirmation")
            builder.setIcon(R.drawable.splash_logo)
            builder.setMessage("Do you want to Start the Test?")
            builder.setPositiveButton(
                "Start"
            ) { dialogInterface: DialogInterface, i: Int ->
                ltInstructions.visibility=View.GONE
                showQuestionsFragment()
                 }
            builder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
            builder.show()



        }

    }


    fun fetchFromAPI() {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val resultsApi = retrofit.create(ResultsApi::class.java)
            val response = resultsApi.getQuestions()
            response.enqueue(object : Callback<ResponseAPI> {
                override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                }

                override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                    val mResponse = response.body()
                    val count = mResponse?.results?.size

                    var resultString =" "
                    count?.let {
                        QuestionsObject.questionList.clear()
                        for (i in 0 until count) {
                            QuestionsObject.questionList.add(mResponse.results[i])
                           /*resultString+= "Question ${i+1}: ${mResponse.results[i].question}\n"
                            resultString+="CorrectAnswer: ${mResponse.results[i].correctAnswer}\n"
                            resultString+="WrongAnswer1: ${mResponse.results[i].incorrectAnswer[0]}\n"
                            resultString+="WrongAnswer2: ${mResponse.results[i].incorrectAnswer[1]}\n"
                            resultString+="WrongAnswer3: ${mResponse.results[i].incorrectAnswer[2]}\n"*/
                        }
                        shuffleQuestions()
                        progressFetchQuestionsLayout.visibility= View.GONE
                        ltInstructions.visibility=View.VISIBLE

                    } ?: Log.i("RESULT_COUNT: ", count.toString())
                    //txtResults.text= resultString

                }

            })
        } catch (e: Exception) {
            Log.e("fetchFromAPI", e.message)
        }
    }

    fun shuffleQuestions(){
        if(QuestionsObject.questionList.size>0){
            Shuffled.questionList.clear()
            QuestionsObject.questionList.shuffle()
            for (i in QuestionsObject.questionList){
                var question= Html.fromHtml(i.question).toString()
                var correctAnswer= Html.fromHtml(i.correctAnswer).toString()
                var allAnswersArrayList: ArrayList<String> = arrayListOf()
                var incorrectAnswers: ArrayList<String> = arrayListOf()
                incorrectAnswers.add(Html.fromHtml(i.incorrectAnswer[0]).toString())
                incorrectAnswers.add(Html.fromHtml(i.incorrectAnswer[1]).toString())
                incorrectAnswers.add(Html.fromHtml(i.incorrectAnswer[2]).toString())
                allAnswersArrayList.addAll(incorrectAnswers)
                allAnswersArrayList.add(Html.fromHtml(i.correctAnswer).toString())
                allAnswersArrayList.shuffle()
                Shuffled.questionList.add(ShuffledQuestions(question, correctAnswer, allAnswersArrayList, incorrectAnswers))
            }
        }
        else Toast.makeText(this,"Shuffling failed as Question list is empty", Toast.LENGTH_SHORT).show()

    }




    fun showQuestionsFragment() {
        val transaction = manager.beginTransaction()
        val fragment = QuestionsFragment()
        transaction.replace(R.id.fragmentTestHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {

    }



    override fun onDestroy() {
        super.onDestroy()


    }


}