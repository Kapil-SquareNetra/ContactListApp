package com.example.contactlistapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.contactlistapp.R
import com.example.contactlistapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_screen.*

class MainScreen: AppCompatActivity() {

    val manager= supportFragmentManager

    val SEARCH_FRAGTAG="Search"
    val SETTING_FRAGTAG="Setting"
    val TEST_FRAGTAG="Test"


    var navListener = BottomNavigationView.OnNavigationItemSelectedListener{
        item -> when(item.itemId){
        R.id.navAllContacts -> {
            if (manager.findFragmentByTag(SEARCH_FRAGTAG)?.isVisible==false)
                showSearchFragment()
            return@OnNavigationItemSelectedListener true
        }
        R.id.navSettings -> {
            if (manager.findFragmentByTag(SETTING_FRAGTAG)?.isVisible==false || manager.findFragmentByTag(SETTING_FRAGTAG)?.isVisible==null)
                showSettingsFragment()
            return@OnNavigationItemSelectedListener true
        }
        R.id.navTestRegistration -> {
            if (manager.findFragmentByTag(TEST_FRAGTAG)?.isVisible==false || manager.findFragmentByTag(TEST_FRAGTAG)?.isVisible==null)
                showCandidateFragment()
            return@OnNavigationItemSelectedListener true
        }

        else -> return@OnNavigationItemSelectedListener false

    }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        val intent:Intent= getIntent()
        bottomNavigationBar.setOnNavigationItemSelectedListener(navListener)
        showSearchFragment()

        when{
            intent?.action== Intent.ACTION_SEND ->{
                if(intent.type=="text/plain"){
                    intent.getStringExtra(Intent.EXTRA_TEXT)?.toString().let {
                        val bundle= Bundle()
                        bundle.putString("PHNUM", it)
                        val transaction= manager.beginTransaction()
                        val fragment= Contact_Fragment()
                        fragment.arguments=bundle
                        transaction.replace(R.id.fragmentHolder, fragment,SEARCH_FRAGTAG)
                        transaction.addToBackStack(null)
                        transaction.commit()

                    }

                }

            }
        }






    }

    override fun onStart() {
        super.onStart()
    }

    fun showSearchFragment(){
        val transaction= manager.beginTransaction()
        val fragment= SearchFragment()
        transaction.replace(R.id.fragmentHolder, fragment,SEARCH_FRAGTAG)
        transaction.commit()
    }

    fun showCandidateFragment(){
        val transaction= manager.beginTransaction()
        val fragment= TestRegistrationFragment()
        transaction.replace(R.id.fragmentHolder, fragment, TEST_FRAGTAG)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun showSettingsFragment(){
        val transaction= manager.beginTransaction()
        val fragment= SettingsFragment()
        transaction.replace(R.id.fragmentHolder, fragment,SETTING_FRAGTAG)
        transaction.addToBackStack(null)
        transaction.commit()

    }


    override fun onBackPressed() {
        val currentFragment:Fragment?=manager.findFragmentByTag(SEARCH_FRAGTAG)
        currentFragment?.let {
            if(currentFragment.isVisible){
                finish()
                return
            }

        }
        manager.popBackStackImmediate()
    }
}