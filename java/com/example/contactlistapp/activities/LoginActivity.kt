package com.example.contactlistapp.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactlistapp.R
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.tables.UserTable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.services.people.v1.PeopleServiceScopes
import kotlinx.android.synthetic.main.login_activity.*

object currentUser {
    var currentUserID: Int = 0
    var currentUserName: String? = ""
    var currentEmailId: String? = ""

}


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    val REQUEST_CODE = 1
    val GOOGLE_SIGN_IN_CODE = 7
    var usersHelper= UsersHelper(this)
    var mUserTable = UserTable(usersHelper)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val contactScope = Scope(PeopleServiceScopes.CONTACTS_READONLY)
        val phoneNumScope = Scope(PeopleServiceScopes.USER_PHONENUMBERS_READ)

        val gsoRequests = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(this.getString(R.string.webClientID))
            .requestScopes(contactScope, phoneNumScope)
            .requestEmail()

        val gso = gsoRequests.build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        btnGoogleSignIn.setOnClickListener {

            val intent: Intent = mGoogleSignInClient.signInIntent
            try{
                startActivityForResult(intent, GOOGLE_SIGN_IN_CODE)
            }
            catch (e: Exception){
                Log.d("btnGoogleSignIn", e.message)
            }

        }


        btnLogin.setOnClickListener {

            if (checkLogin()) {
                goToAppActivity()

            } else Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        }

        txtSignup.setOnClickListener {
            var intent = Intent(this, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            val res: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(res)
        }
    }


    fun handleSignInResult(result: Task<GoogleSignInAccount>) {
        val account: GoogleSignInAccount?
        var authCode: String?
        try {


            account = result.getResult(ApiException::class.java)

            authCode = account!!.serverAuthCode

            val userName = account?.displayName
            val userEmail = account?.email
            if (userName != null && userEmail != null) {
                var result = mUserTable.findUserData(
                    userName = userName.toString(),
                    email = userEmail.toString()
                )
                result?.let{
                    if (result.size <= 0) {
                        mUserTable.insertUserData(
                            userName = userName.toString(),
                            email = userEmail.toString()
                        )
                    }
                }

                var currentUserResult = mUserTable.findUserData(
                    userName = userName.toString(),
                    email = userEmail.toString()
                )
                currentUserResult?.let {
                    currentUser.currentUserID = currentUserResult[0].userID.toInt()
                    currentUser.currentUserName = currentUserResult[0].userName
                    currentUser.currentEmailId = currentUserResult[0].userEmail
                }
                saveToSharedPref(
                    userName = userName.toString(),
                    email = userEmail.toString(),
                    googleLogin = true,
                    auth = authCode
                )
                goToAppActivity()



            } else Toast.makeText(this, "Google Login Failed!", Toast.LENGTH_SHORT).show()

        } catch (e: ApiException) {
            Log.e("Error", "signInResult:failed code=" + e.getStatusCode());
        } catch (e: Exception) {
            Log.e("unknown error", e?.message ?: "unknown")
        }


    }


    fun checkLogin(): Boolean {

        var userName = edtUserName.text.toString()
        var userPassword = edtPassword.text.toString()


        var result = mUserTable.findUserData(userName = userName, password = userPassword)

        result?.let {
        if (result.size > 0) {
                saveToSharedPref(userName = userName, password = userPassword, googleLogin = false)
                return true

        }
        }
        return false

    }

    fun saveToSharedPref(
        userName: String,
        email: String? = null,
        password: String? = null,
        googleLogin: Boolean = false,
        auth: String?= null
    ) {

        val preferences: SharedPreferences =
            this.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt("Current_User_ID", currentUser.currentUserID)
        editor.putString("Current_User_Name", userName)
        editor.putString("Current_User_Email", email)
        editor.putString("Current_User_PW", password)
        editor.putBoolean("Login_Status", true)
        editor.putBoolean("Google_Login", googleLogin)
        editor.putString("AUTH",auth)
        editor.apply()
        editor.commit()

    }


    fun goToAppActivity() {
        var intent = Intent(this, MainScreen::class.java)
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        val mGoogleApiAvailability = GoogleApiAvailability.getInstance()
        val dialog: Dialog = mGoogleApiAvailability.getErrorDialog(this, p0.errorCode, 1)
        dialog.show()


    }

}