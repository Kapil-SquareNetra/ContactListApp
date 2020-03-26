package com.example.contactlistapp.fragments

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.contactlistapp.R
import com.example.contactlistapp.activities.correctAnswers
import com.example.contactlistapp.activities.unAttendedAnswer
import com.example.contactlistapp.activities.wrongAnswers
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.models.Shuffled
import com.example.contactlistapp.models.ShuffledQuestions
import com.example.contactlistapp.tables.QuestionsTable
import kotlinx.android.synthetic.main.fragment_questionnaire.*

class QuestionsFragment : Fragment(){

    var questionNo: Int= -1
    val TIME_CONST: Long= 1200000
    var TIME_LEFT: Long= TIME_CONST
    var isTimerRunning: Boolean= false
    lateinit var countDownTimer: CountDownTimer
    var isTestFinished: Boolean= false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_right)
        enterTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_questionnaire, container, false)
        val tbQuestionnaire = view.findViewById<Toolbar>(R.id.tbQuestionnaire)
        tbQuestionnaire.inflateMenu(R.menu.timer_menu)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTestFinished= false
        questionNo+=1
        countTimer()
        populateQuestion()

        requireActivity().txtUnAnswered.text= "Un Attended: 20"
        requireActivity().txtAnswered.text= "Attended: 0"

        btnNextQuestion.setOnClickListener {
            storeResponse()
            setQuestionStatus()
            if (questionNo<=19 && questionNo>=0){
                nextQuestion()
            }
        }

        btnPreviousQuestion.setOnClickListener {
            storeResponse()
            setQuestionStatus()
            if(questionNo>0 && questionNo<=19){
                previousQuestion()
            }

        }
        var timer= requireActivity().findViewById<View>(R.id.itemTimer) as TextView
        timer.text="20:00"
    }

    fun calculateResults(){
        correctAnswers=0
        unAttendedAnswer=0
        wrongAnswers=0
        storeResponse()

        for(i in Shuffled.questionList){
            if (i.candidateSelection==i.correctAnswer) correctAnswers++
            else if (i.candidateSelection==null) unAttendedAnswer++
            else wrongAnswers++
        }

        saveResponsesToDb()

        var resultIntent= Intent()
        resultIntent.putExtra("correctAnswers",correctAnswers)
        resultIntent.putExtra("wrongAnswers",wrongAnswers)
        resultIntent.putExtra("unAttendedAnswer",unAttendedAnswer)
       requireActivity().setResult(Activity.RESULT_OK,  resultIntent)
        requireActivity().finish()

    }
    fun saveResponsesToDb(){
        val usersHelper= UsersHelper(requireActivity())
        val questionsTable= QuestionsTable(usersHelper)
        for(i in Shuffled.questionList){
            questionsTable.insertQuestionData(currentCandidateID,i.question,i.correctAnswer,
                i.wrongAnswers[0], i.wrongAnswers[1], i.wrongAnswers[2], i.candidateSelection)
        }

        Toast.makeText(requireActivity(),"Database Save completed", Toast.LENGTH_SHORT).show()
    }

    fun populateQuestion(){
        rgAnswers.clearCheck()
        txtQuestionNo.text= (questionNo+1).toString()
        txtQuestion.text= Shuffled.questionList[questionNo].question
        rbAnswer1.text=Shuffled.questionList[questionNo].answerOptionsArray[0]
        rbAnswer2.text=Shuffled.questionList[questionNo].answerOptionsArray[1]
        rbAnswer3.text=Shuffled.questionList[questionNo].answerOptionsArray[2]
        rbAnswer4.text=Shuffled.questionList[questionNo].answerOptionsArray[3]
        when {
            questionNo<=0 -> {
                btnPreviousQuestion.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                btnPreviousQuestion.visibility= View.INVISIBLE
                btnPreviousQuestion.isEnabled=false
                btnNextQuestion.text="Next"
            }
            questionNo>=19 -> {
                btnPreviousQuestion.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                btnPreviousQuestion.visibility= View.VISIBLE
                btnPreviousQuestion.isEnabled=true
                btnNextQuestion.text="Submit"
            }
            else -> {
                btnPreviousQuestion.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                btnPreviousQuestion.visibility= View.VISIBLE
                btnPreviousQuestion.isEnabled=true
                btnNextQuestion.text="Next"
            }
        }
    }

    fun previousQuestion(){
        questionNo-=1
        populateQuestion()
        if(Shuffled.questionList[questionNo].candidateSelection!=null){
            when {
                rbAnswer1.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer1.isChecked=true
                }
                rbAnswer2.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer2.isChecked=true
                }
                rbAnswer3.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer3.isChecked=true
                }
                rbAnswer4.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer4.isChecked=true
                }
            }
        }

    }

    fun nextQuestion(){
        if (btnNextQuestion.text=="Submit"){
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Submit Answers confirmation")
            builder.setIcon(R.drawable.splash_logo)
            builder.setMessage("Do you want to Submit your responses?")
            builder.setPositiveButton(
                "Submit"
            ) { dialogInterface: DialogInterface, i: Int ->
                isTestFinished=true
                calculateResults()
                countDownTimer.cancel()
                isTimerRunning=false
            }
            builder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
            builder.show()
            return
        }


        changeQuestionLabelStatus()
        questionNo+=1
        populateQuestion()
        if(Shuffled.questionList[questionNo].candidateSelection!=null){
            when {
                rbAnswer1.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer1.isChecked=true
                }
                rbAnswer2.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer2.isChecked=true
                }
                rbAnswer3.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer3.isChecked=true
                }
                rbAnswer4.text== Shuffled.questionList[questionNo].candidateSelection -> {
                    rbAnswer4.isChecked=true
                }
            }
        }
    }

    fun changeQuestionLabelStatus(){
        val label= getQuestionLabel(questionNo)
        if (Shuffled.questionList[questionNo].candidateSelection.isNullOrEmpty())
            label.background=requireActivity().getDrawable(R.drawable.small_circle_red)
        else
            label.background=requireActivity().getDrawable(R.drawable.small_circle_green)
    }

    fun setQuestionStatus(){

        var attended: List<ShuffledQuestions> = Shuffled.questionList.filter { !it.candidateSelection.isNullOrEmpty()}
        requireActivity().txtAnswered.text= "Attended: ${attended.size}"

        var unattended: List<ShuffledQuestions> = Shuffled.questionList.filter { it.candidateSelection.isNullOrEmpty()}
        requireActivity().txtUnAnswered.text= "Un Attended: ${unattended.size}"
    }



    fun storeResponse(){
        when(rgAnswers.checkedRadioButtonId){
            R.id.rbAnswer1 -> {
                Shuffled.questionList[questionNo].candidateSelection= rbAnswer1.text.toString()
            }
            R.id.rbAnswer2 -> {
                Shuffled.questionList[questionNo].candidateSelection= rbAnswer2.text.toString()
            }
            R.id.rbAnswer3 -> {
                Shuffled.questionList[questionNo].candidateSelection= rbAnswer3.text.toString()
            }
            R.id.rbAnswer4 -> {
                Shuffled.questionList[questionNo].candidateSelection= rbAnswer4.text.toString()
            }
        }
    }

    fun countTimer(){
         countDownTimer= object : CountDownTimer(TIME_LEFT, 1000){
            override fun onFinish() {
                isTestFinished= true
                calculateResults()
            }

            override fun onTick(millisUntilFinished: Long) {
                TIME_LEFT= millisUntilFinished
                updateTimerText()
            }

        }.start()
        isTimerRunning= true
    }


    fun updateTimerText(){
        var minutes: Int= ((TIME_LEFT / 1000)/ 60).toInt()
        var seconds: Int= ((TIME_LEFT/1000)%60).toInt()

        var formatMinutes= minutes.toString().padStart(2,'0')
        var formatSeconds= seconds.toString().padStart(2,'0')

        var timerText="$formatMinutes:$formatSeconds"
        var timer= requireActivity().findViewById<View>(R.id.itemTimer) as TextView
        timer.text=timerText

    }


    fun getQuestionLabel(questionNum: Int): View{

        var labelView: View

        when(questionNum+1){
            1->labelView=requireActivity().findViewById(R.id.txtQuestion1)
            2->labelView=requireActivity().findViewById(R.id.txtQuestion2)
            3->labelView=requireActivity().findViewById(R.id.txtQuestion3)
            4->labelView=requireActivity().findViewById(R.id.txtQuestion4)
            5->labelView=requireActivity().findViewById(R.id.txtQuestion5)
            6->labelView=requireActivity().findViewById(R.id.txtQuestion6)
            7->labelView=requireActivity().findViewById(R.id.txtQuestion7)
            8->labelView=requireActivity().findViewById(R.id.txtQuestion8)
            9->labelView=requireActivity().findViewById(R.id.txtQuestion9)
            10->labelView=requireActivity().findViewById(R.id.txtQuestion10)
            11->labelView=requireActivity().findViewById(R.id.txtQuestion11)
            12->labelView=requireActivity().findViewById(R.id.txtQuestion12)
            13->labelView=requireActivity().findViewById(R.id.txtQuestion13)
            14->labelView=requireActivity().findViewById(R.id.txtQuestion14)
            15->labelView=requireActivity().findViewById(R.id.txtQuestion15)
            16->labelView=requireActivity().findViewById(R.id.txtQuestion16)
            17->labelView=requireActivity().findViewById(R.id.txtQuestion17)
            18->labelView=requireActivity().findViewById(R.id.txtQuestion18)
            19->labelView=requireActivity().findViewById(R.id.txtQuestion19)
            20->labelView=requireActivity().findViewById(R.id.txtQuestion20)
            else -> labelView=requireActivity().findViewById(R.id.txtQuestion1)
        }

        return labelView
    }

    override fun onPause() {
        super.onPause()
        val activityManager: ActivityManager = requireActivity().applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.moveTaskToFront(requireActivity().taskId,0)
        if (!isTestFinished){
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Submit Answers confirmation")
            builder.setIcon(R.drawable.ic_error)
            builder.setMessage("Test will be Submitted, Do you want to continue?")
            builder.setPositiveButton(
                "End Test"
            ) { dialogInterface: DialogInterface, i: Int -> calculateResults()
                countDownTimer.cancel()
                isTimerRunning=false
            }
            builder.setNegativeButton("Continue") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(requireActivity(),"Finish the Test First!!!!", Toast.LENGTH_SHORT).show()}
            builder.show()
        }





    }




}