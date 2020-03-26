package com.example.contactlistapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.contactlistapp.R
import com.example.contactlistapp.activities.QuestionnaireActivity
import com.example.contactlistapp.activities.correctAnswers
import com.example.contactlistapp.activities.unAttendedAnswer
import com.example.contactlistapp.activities.wrongAnswers
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.models.CandidateDetails
import com.example.contactlistapp.tables.CandidateTable
import kotlinx.android.synthetic.main.fragment_test_registration.*

var currentCandidateID: Int = 0
const val TEST_REQUEST_CODE= 666

class TestRegistrationFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        enterTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test_registration, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txtResultsTitle.visibility=View.INVISIBLE
        txtCorrectAnswers.visibility=View.INVISIBLE
        txtWrongAnswers.visibility=View.INVISIBLE
        txtUnattendedAnswers.visibility=View.INVISIBLE
        btnStartTest.isEnabled=true
        btnStartTest.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))

        btnStartTest.setOnClickListener {

            if (validateUserDetails()) {
                if (saveCandidateDetails()) {
                    getcurrentCandidateID()

                } else Toast.makeText(
                    requireActivity(),
                    "Candidate Login Failed!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    fun validateUserDetails(): Boolean {
        val usersHelper = UsersHelper(requireActivity())
        val candidateTable = CandidateTable(usersHelper)
        val pattern= Regex(".+\\@.+\\..+")

        if (edtCandidateName.text.isNullOrEmpty()) {
            edtltCandidateName.error = "Candidate Name Cannot be Empty"
            return false
        } else edtltCandidateName.error = null

        if (edtCandidateEmailID.text.isNullOrEmpty()) {
            edtltCandidateEmailID.error = "Email ID Cannot be Empty"
            return false
        }
        else if(!pattern.containsMatchIn(edtCandidateEmailID.text.toString())){
            edtltCandidateEmailID.error = "Invalid email"
            return false
        }

        else edtltCandidateEmailID.error = null

        if (edtCandidatePhone.text.isNullOrEmpty()) {
            edtltCandidatePhone.error = "New Password Cannot be Empty"
            return false
        }
        else if (edtCandidatePhone.text.toString().length !in 10..16){
            edtltCandidatePhone.error = "Invalid length of phone number"
            return false
        }

        else edtltCandidatePhone.error = null
        var list: MutableList<CandidateDetails>? = candidateTable.getCurrentCandidate(
            edtCandidateName.text.toString(),
            edtCandidateEmailID.text.toString(), edtCandidatePhone.text.toString()
        )
        if (!list.isNullOrEmpty()) {
            edtltCandidatePhone.error="Candidate with same details already registered"
            return false
        }
        else edtltCandidatePhone.error=null
        return true
    }

    fun saveCandidateDetails(): Boolean {

        val usersHelper = UsersHelper(requireActivity())
        val candidateTable = CandidateTable(usersHelper)

        var candidateName: String = edtCandidateName.text.toString()
        var candidateEmail: String = edtCandidateEmailID.text.toString()
        var candidatePhone: String = edtCandidatePhone.text.toString()

        if (candidateTable.insertCandidateData(candidateName, candidateEmail, candidatePhone))
            return true
        else return false
    }

    fun getcurrentCandidateID(){
        val usersHelper = UsersHelper(requireActivity())
        val candidateTable = CandidateTable(usersHelper)
        var list: MutableList<CandidateDetails>? = candidateTable.getCurrentCandidate(
            edtCandidateName.text.toString(),
            edtCandidateEmailID.text.toString(), edtCandidatePhone.text.toString()
        )
        if (!list.isNullOrEmpty()) {
            currentCandidateID= list[0].candidateID.toInt()
            goToQuestions()
        } else Toast.makeText(requireActivity(),"Current candidate details not available", Toast.LENGTH_SHORT).show()
    }

    fun goToQuestions() {
        val intent = Intent(requireActivity(), QuestionnaireActivity::class.java)
        startActivityForResult(intent, TEST_REQUEST_CODE )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TEST_REQUEST_CODE) {
            if (resultCode==Activity.RESULT_OK){
                btnStartTest.isEnabled=false
                btnStartTest.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                txtResultsTitle.visibility= View.VISIBLE
                txtCorrectAnswers.visibility=View.VISIBLE
                txtWrongAnswers.visibility=View.VISIBLE
                txtUnattendedAnswers.visibility=View.VISIBLE
                if (correctAnswers!=0){

                    txtCorrectAnswers.text= "Correct: $correctAnswers"
                }
                if (wrongAnswers!=0){

                    txtWrongAnswers.text= "Incorrect: $wrongAnswers"
                }
                if (unAttendedAnswer!=0){

                    txtUnattendedAnswers.text= "UnAttended: $unAttendedAnswer"
                }
            }

        }
    }

}