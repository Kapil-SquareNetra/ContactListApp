package com.example.contactlistapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.contactlistapp.R
import com.example.contactlistapp.models.CandidateList
import com.example.contactlistapp.models.candidateSupplier
import kotlinx.android.synthetic.main.activity_display_candidate.*

class DisplayCandidateActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_candidate)
        val intent= intent
        var  currentCandidate= intent.getIntExtra("clicked_position",0)
            setCandidateResultData(currentCandidate)

    }

    fun setCandidateResultData(pos: Int){
        var correctAnswerCount=0
        var wrongAnswerCount=0
        var unAnsweredCount=0

        var candidate :CandidateList= candidateSupplier.candidateItems[pos]
        txtCandidateDisplayName.text= candidate.candidateName
        txtCandidateDisplayEmail.text= candidate.candidateEmail
        txtCandidatePhoneNumber.text= candidate.candidateNumber
        var resposeArray= candidate.answerList
            if (!resposeArray.isNullOrEmpty()){
                for (i in resposeArray.indices){
                    if(resposeArray[i].candidateResponse.isNullOrEmpty()){
                        unAnsweredCount++
                        getQuestionLabel(i).background=getDrawable(R.drawable.small_circle)
                        continue
                    }
                    if (resposeArray[i].correctAnswer==resposeArray[i].candidateResponse){
                        getQuestionLabel(i).background=getDrawable(R.drawable.small_circle_green)
                        correctAnswerCount++
                    }
                    else{
                        getQuestionLabel(i).background=getDrawable(R.drawable.small_circle_red)
                        wrongAnswerCount++
                    }

                }
            }

        txtCorrectResult.text= "Correct Answers: $correctAnswerCount"
        txtWrongResult.text="Wrong Answers: $wrongAnswerCount"
        txtUnAnsweredResult.text="Un-Answered: $unAnsweredCount"

    }

    fun getQuestionLabel(questionNum: Int): View {

        var labelView: View

        when(questionNum+1){
            1->labelView=findViewById(R.id.txtResultQuestion1)
            2->labelView=findViewById(R.id.txtResultQuestion2)
            3->labelView=findViewById(R.id.txtResultQuestion3)
            4->labelView=findViewById(R.id.txtResultQuestion4)
            5->labelView=findViewById(R.id.txtResultQuestion5)
            6->labelView=findViewById(R.id.txtResultQuestion6)
            7->labelView=findViewById(R.id.txtResultQuestion7)
            8->labelView=findViewById(R.id.txtResultQuestion8)
            9->labelView=findViewById(R.id.txtResultQuestion9)
            10->labelView=findViewById(R.id.txtResultQuestion10)
            11->labelView=findViewById(R.id.txtResultQuestion11)
            12->labelView=findViewById(R.id.txtResultQuestion12)
            13->labelView=findViewById(R.id.txtResultQuestion13)
            14->labelView=findViewById(R.id.txtResultQuestion14)
            15->labelView=findViewById(R.id.txtResultQuestion15)
            16->labelView=findViewById(R.id.txtResultQuestion16)
            17->labelView=findViewById(R.id.txtResultQuestion17)
            18->labelView=findViewById(R.id.txtResultQuestion18)
            19->labelView=findViewById(R.id.txtResultQuestion19)
            20->labelView=findViewById(R.id.txtResultQuestion20)
            else -> labelView=findViewById(R.id.txtResultQuestion1)
        }

        return labelView
    }

}
