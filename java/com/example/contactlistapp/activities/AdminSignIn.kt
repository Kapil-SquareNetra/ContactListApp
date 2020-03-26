package com.example.contactlistapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contactlistapp.R
import kotlinx.android.synthetic.main.activity_admin_sign_in.*

class AdminSignIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_in)


        btnAdminLogin.setOnClickListener {
            if(validateAdminLogin()){
                if (edtAdminName.text.toString()=="ADMIN".toLowerCase()&& edtAdminPassword.text.toString()=="FULL".toLowerCase()){
                    val intent= Intent(this,CandidateActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }
    }



    fun validateAdminLogin():Boolean{
        if (edtAdminName.text.isNullOrEmpty()){
            edtLtAdminName.error="Name Cannot be empty!!"
            return false
        }
        else
            edtLtAdminName.error=null

        if (edtAdminPassword.text.isNullOrEmpty()){
            edtLtAdminPassword.error="Password Cannot be empty!!"
            return false
        }
        else
            edtLtAdminPassword.error=null

        if(edtAdminName.text.toString()!="ADMIN".toLowerCase()|| edtAdminPassword.text.toString()!="FULL".toLowerCase()){
            edtLtAdminPassword.error="Incorrect Name/Password"
            return false
        }
        else{
            edtLtAdminPassword.error=null
        }
        return true
    }
}
