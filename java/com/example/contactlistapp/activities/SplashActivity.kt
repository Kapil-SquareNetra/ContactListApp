package com.example.contactlistapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.contactlistapp.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {
    val SPLASH_ACTIVITY_TIMER: Long = 500


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val imageView:ImageView=findViewById(R.id.imageView)
        Glide.with(this)
            .load(R.drawable.splash_logo)
            .circleCrop()
            .into(imageView)


        val preferences: SharedPreferences = this.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val loginStatus= preferences.getBoolean("Login_Status",false)
        currentUser.currentUserID=preferences.getInt("Current_User_ID",0)
        currentUser.currentUserName= preferences.getString("Current_User_Name","")
        currentUser.currentEmailId= preferences.getString("Current_User_Email","")

        val background= object : Thread(){

            override fun run() {
                try {
                    sleep(SPLASH_ACTIVITY_TIMER)
                    loginChecker(loginStatus)
                    System.out.println("completed")
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
        System.out.println("after thread ")
    }




    fun loginChecker(loginStatus: Boolean){
        val intent: Intent
        if(loginStatus)
            intent= Intent(this, MainScreen::class.java)
        else
            intent=Intent(this, LoginActivity::class.java)


        startActivity(intent)
        finish()

    }

}