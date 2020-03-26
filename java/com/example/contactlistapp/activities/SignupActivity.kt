package com.example.contactlistapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactlistapp.R
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.tables.UserTable
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    var usersHelper = UsersHelper(this)
    var mUserTable = UserTable(usersHelper)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btnRegister.setOnClickListener {
            if (validateUserDetails()) {
                saveToDataBase()
                finish()
            }
        }

        txtLogin.setOnClickListener {
            finish()
        }


    }


    fun validateUserDetails(): Boolean {
        val pattern = Regex(".+\\@.+\\..+")

        if (edtNewUserName.text.isNullOrEmpty()) {
            edtltNewUserName.error = "User Name Cannot be Empty"
            return false
        } else edtltNewUserName.error = null

        if (edtNewEmailID.text.isNullOrEmpty()) {
            edtltNewEmailID.error = "Email ID Cannot be Empty"
            return false
        } else if (!pattern.containsMatchIn(edtNewEmailID.text.toString())) {
            edtltNewEmailID.error = "Invalid email"
            return false
        } else edtltNewEmailID.error = null

        if (edtNewPassword.text.isNullOrEmpty()) {
            edtltNewPassword.error = "New Password Cannot be Empty"
            return false
        } else edtltNewPassword.error = null

        if (edtConfirmPassword.text.isNullOrEmpty()) {
            edtltConfirmPassword.error = "Confirm Password Cannot be Empty"
            return false
        } else edtltConfirmPassword.error = null

        if (edtNewPassword.text.toString() != edtConfirmPassword.text.toString()) {
            edtltConfirmPassword.error = "Passwords not matching"
            return false
        } else edtltConfirmPassword.error = null

        return true
    }

    fun saveToDataBase() {

        var userName = edtNewUserName.text.toString()
        var email = edtNewEmailID.text.toString()
        var password = edtConfirmPassword.text.toString()

        var isSaved: Boolean = mUserTable.insertUserData(userName, email, password)
        if (isSaved) Toast.makeText(this, "User Details Saved!", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, "Save Operation failed ", Toast.LENGTH_SHORT).show()


    }

}