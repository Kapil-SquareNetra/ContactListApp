package com.example.contactlistapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.contactlistapp.R
import com.example.contactlistapp.activities.AdminSignIn
import com.example.contactlistapp.activities.LoginActivity
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.tables.*
import com.example.contactlistapp.workerClass.GoogleSyncWorker
import com.example.contactlistapp.workerClass.PhoneSyncClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.people.v1.PeopleServiceScopes
import kotlinx.android.synthetic.main.fragment_sync.*

class SettingsFragment : Fragment() {
    val GOOGLE_CODE = 21
    var REQUEST_FLAG = false




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
        val view = inflater.inflate(R.layout.fragment_sync, container, false)
        val tbSettings = view.findViewById<Toolbar>(R.id.tbSettings)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tbSettings.inflateMenu(R.menu.settings_menu)
        }
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Shared Preference Fetch
        val pref: SharedPreferences = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val isGoogleSyncOn = pref.getBoolean("GoogleSync", false)
        val isPhoneSyncOn = pref.getBoolean("PhoneSync", false)
        val authCode = pref.getString("AUTH", null)

        if (isGoogleSyncOn) switchGoogleSync.isChecked = true
        if (isPhoneSyncOn) switchPhoneSync.isChecked = true

        //shared Preference change listener
        sharedPrefChange(pref)

        //click events in menu
        menuClickListener(authCode)

        //Switch listener
        switchListeners()

        btnAdminAccess.setOnClickListener {
            val intent = Intent(requireActivity(), AdminSignIn::class.java)
            startActivity(intent)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_CODE) {
                Toast.makeText(requireActivity(), "Scopes Added Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Denied by user")
                    REQUEST_FLAG = false
                } else {
                    Log.d("Permission", "Granted By User")
                    REQUEST_FLAG = true
                    //ImportFromPhone()
                }

            }
        }
    }





    fun sharedPrefChange(pref:SharedPreferences ){
        val contactScope = Scope(PeopleServiceScopes.CONTACTS_READONLY)
        val phoneNumScope = Scope(PeopleServiceScopes.USER_PHONENUMBERS_READ)

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { pref, key ->
            /*if (key == "Login_Status") {
                val curStatus = pref.getBoolean(key, false)
                if (!curStatus) {
                }
            }*/

            if (key == "GoogleSync") {
                val GoogleSyncStatus = pref.getBoolean(key, false)
                if (GoogleSyncStatus == true) {
                    if (GoogleSignIn.hasPermissions(
                            GoogleSignIn.getLastSignedInAccount(
                                requireActivity()
                            ), contactScope, phoneNumScope
                        )
                    ) {

                    } else
                        Toast.makeText(
                            requireActivity(),
                            "Sign in using Google First",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }
        pref.registerOnSharedPreferenceChangeListener(listener)
    }

    fun switchListeners() {
        switchGoogleSync.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val pref: SharedPreferences =
                    requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putBoolean("GoogleSync", true)
                editor.apply()
            } else {
                val pref: SharedPreferences =
                    requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putBoolean("GoogleSync", false)
                editor.apply()
            }
        }
        switchPhoneSync.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                val pref: SharedPreferences =
                    requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putBoolean("PhoneSync", true)
                editor.apply()
            } else {
                val pref: SharedPreferences =
                    requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putBoolean("PhoneSync", false)
                editor.apply()
            }


        }
    }

    fun menuClickListener(authCode: String?){
        var notificationManager: NotificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("k1", "Google", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)

            val phoneChannel =
                NotificationChannel("k2", "Phone", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(phoneChannel)
        }

        val builderBefore: NotificationCompat.Builder =
            NotificationCompat.Builder(requireActivity(), "k1")
                .setContentTitle("Google Sync")
                .setContentText("Sync Started")
                .setSmallIcon(R.drawable.ninja).setColor(resources.getColor(R.color.colorAccent))

        val phoneBuilderBefore: NotificationCompat.Builder =
            NotificationCompat.Builder(requireActivity(), "k2")
                .setContentTitle("Phone Sync")
                .setContentText("Sync Started")
                .setSmallIcon(R.drawable.ninja).setColor(resources.getColor(R.color.colorAccent))

        tbSettings.setOnMenuItemClickListener {
            if (it.itemId == R.id.itemLogout) {
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("Log-out Confirmation")
                builder.setIcon(R.drawable.off)
                builder.setMessage("Are you sure you want to logout?")
                builder.setPositiveButton(
                    "Logout",
                    { dialogInterface: DialogInterface, i: Int -> logOutFromSharedPref() })
                builder.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }
            if (it.itemId == R.id.itemSyncNow) {

                if (switchGoogleSync.isChecked) {
                    if (authCode != null) {
                        val code = Data.Builder().putString("AUTH", authCode).build()
                        val constraints =
                            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        var request: OneTimeWorkRequest? =
                            OneTimeWorkRequest.Builder(GoogleSyncWorker::class.java)
                                .setConstraints(Constraints(constraints))
                                .setInputData(code)
                                .build()

                        request?.let {
                            WorkManager.getInstance(requireActivity()).enqueue(request)
                            notificationManager.notify(1, builderBefore.build())


                            WorkManager.getInstance(requireActivity())
                                .getWorkInfoByIdLiveData(request.id)
                                .observe(requireActivity(),
                                    Observer {



                                        if (it.state.isFinished) {
                                            var builderAfter: NotificationCompat.Builder
                                            if (it.state==WorkInfo.State.SUCCEEDED){
                                                builderAfter =
                                                    NotificationCompat.Builder(requireActivity(), "k1")
                                                        .setContentTitle("Google Sync")
                                                        .setContentText("Sync Finished")
                                                        .setSmallIcon(R.drawable.ninja)
                                                        .setColor(resources.getColor(R.color.colorAccent))
                                            }
                                            else builderAfter =
                                                    NotificationCompat.Builder(requireActivity(), "k1")
                                                        .setContentTitle("Google Sync")
                                                        .setContentText("Sync Failed - Login Again and retry!")
                                                        .setSmallIcon(R.drawable.ninja)
                                                        .setColor(resources.getColor(R.color.colorAccent))

                                            notificationManager.notify(1, builderAfter.build())
                                        }
                                    })
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Sign In again to sync with google",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                if (switchPhoneSync.isChecked) {
                    requestPermission()
                    if (REQUEST_FLAG) {
                        var phoneWork =
                            OneTimeWorkRequest.Builder(PhoneSyncClass::class.java).build()
                        WorkManager.getInstance(requireActivity()).enqueue(phoneWork)
                        notificationManager.notify(2, phoneBuilderBefore.build())

                        WorkManager.getInstance(requireActivity())
                            .getWorkInfoByIdLiveData(phoneWork.id)
                            .observe(requireActivity(),
                                Observer {
                                    if (it.state.isFinished) {
                                        val phoneBuilderAfter: NotificationCompat.Builder =
                                            NotificationCompat.Builder(requireActivity(), "k1")
                                                .setContentTitle("Phone Sync")
                                                .setContentText("Sync Finished")
                                                .setSmallIcon(R.drawable.ninja)
                                                .setColor(resources.getColor(R.color.colorAccent))
                                        notificationManager.notify(2, phoneBuilderAfter.build())
                                    }
                                })


                    }
                }


            }


            true
        }

    }

    fun logOutFromSharedPref() {
        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putBoolean("Login_Status", false)
        editor.putBoolean("GoogleSync", false)
        editor.putBoolean("PhoneSync", false)
        editor.apply()
        editor.commit()
        removeFromDatabase()
        logOutFromGoogle(pref.getBoolean("Google_Login", false))
        goToLoginPage()
    }

    fun goToLoginPage() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(requireActivity(), "Signing off....", Toast.LENGTH_SHORT).show()
        requireActivity().finish()
    }

    fun logOutFromGoogle(googleLogin: Boolean) {
        if (googleLogin) {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            googleSignInClient.revokeAccess()
            googleSignInClient.signOut()
        }

    }

    fun removeFromDatabase() {

        var usersHelper = UsersHelper(requireActivity())
        var mContactsTable = ContactsTable(usersHelper)
        var mNumbersTable = NumbersTable(usersHelper)
        var mEmailsTable = EmailsTable(usersHelper)
        var mAddressTable = AddressTable(usersHelper)
        var mIMTable = IMTable(usersHelper)
        var mIMPDatesTable = IMPDatesTable(usersHelper)
        var mCircleTable = CircleTable(usersHelper)

        mContactsTable.deleteContactData()

        mNumbersTable.deleteContactData()

        mEmailsTable.deleteContactData()

        mAddressTable.deleteContactData()

        mIMTable.deleteContactData()

        mIMPDatesTable.deleteContactData()

        mCircleTable.deleteContactData()


    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_CONTACTS
            )
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Denied!!!!")
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(android.Manifest.permission.READ_CONTACTS),
                    PERMISSION_REQUEST
                )
            } else {
                //ImportFromPhone()
                REQUEST_FLAG = true
                return
            }
        } else {
            //ImportFromPhone()
            REQUEST_FLAG = true
            return
        }

    }





}