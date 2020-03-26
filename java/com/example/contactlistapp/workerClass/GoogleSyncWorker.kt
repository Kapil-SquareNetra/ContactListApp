package com.example.contactlistapp.workerClass

import android.content.Context
import android.widget.Toast
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contactlistapp.R
import com.example.contactlistapp.activities.currentUser
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.tables.ContactsTable
import com.example.contactlistapp.tables.NumbersTable
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.model.ListConnectionsResponse
import com.google.api.services.people.v1.model.Person
import com.google.api.services.people.v1.model.PhoneNumber
import java.io.IOException

class GoogleSyncWorker(context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {

    val con= context


    override fun doWork(): Result {

        val data: Data = inputData
        var authCode= data.getString("AUTH")
        if (authCode!=null){
           if (fetchContactFromGoogle(authCode))
               return Result.success()
            else
               return Result.failure()
        }
        else{
            return Result.failure()
        }
    }




    fun fetchContactFromGoogle(authCode: String):Boolean {

        var usersHelper = UsersHelper(con)
        //val t1 = object : Thread() {
        //override fun run() {


        var mContactsTable = ContactsTable(usersHelper)
        var mNumbersTable = NumbersTable(usersHelper)
        var nextPageToken: String? = ""

        try {
            val people = setUp(serverAuthCode = authCode)

            do {
                val response: ListConnectionsResponse =
                    people.People().connections().list("people/me")
                        .setPageToken(nextPageToken)
                        .setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers")
                        .setPageSize(30).setSortOrder("FIRST_NAME_ASCENDING").execute()
                val totalItems = response.totalItems
                val connections: List<Person> = response.connections
                nextPageToken = response.nextPageToken


                for (contact in connections) {
                    if (!contact.isEmpty()) {
                        contact.phoneNumbers?.let {
                            //var currContact= Contacts()
                            val phone: List<PhoneNumber> = contact.phoneNumbers
                            val contactName = contact.names
                            var displayName: String = "No Name"
                            /*var phoneticName: String?=null
                            var nickName: String?=null*/
                            if (contactName != null) {
                                displayName = contactName.get(0)?.displayName ?: "No Name"
                                displayName = displayName.replace("\'","",false)
                                // phoneticName= contactName.get(0)?.phoneticFullName
                            }
                            /* val contactNickname= contact.nicknames
                             if(contactNickname!=null){
                                 nickName= contactNickname.get(0)?.value
                             }*/


                            //var phoneHash = hashMapOf<String, String>()
                            if (phone != null) {
                                val contactID = getcontactID()
                                if (validateValues(displayName)) {
                                    if (contactID > 0) {
                                        if (mContactsTable.insertContactData(
                                                contactID,
                                                currentUser.currentUserID,
                                                displayName
                                            )
                                        ) {
                                            for (number in phone) {
                                                if (mNumbersTable.insertNumberData(
                                                        contactID,
                                                        number.type ?: "other",
                                                        number?.value ?: "No Number"
                                                    )
                                                )
                                                    continue
                                                else println("Number Data error")
                                            }

                                            /* phoneHash.put(number?.type?:"other", number?.value?:"No Number")
                                             currContact.phoneMap = phoneHash*/
                                            //println(" ${number?.type?:"other"}: ${number?.value?:""}")
                                        }

                                    } else println("contact Data error")
                                }

                            }

                        }

                    }

                }

            } while (nextPageToken != null)
            return true
        }
        catch (e: Exception) {
            println(e.printStackTrace())
            return false
        }
    }

    fun getcontactID(): Int {
        var usersHelper= UsersHelper(con)
        var mContactsTable = ContactsTable(usersHelper)
        val idResult= mContactsTable.getContactsID()
        idResult?.let {
            if (idResult.size > 0) {

                var contactID: String = idResult?.get(0)
                var ID = contactID.toInt()
                if (ID?.equals(null) == true) {
                    ID = 1
                } else {
                    ID = contactID.toInt()
                    ID += 1
                }
                return ID

            }
        }
        return 0
    }

    fun validateValues(displayName: String): Boolean {
        var usersHelper= UsersHelper(con)
        var mContactsTable = ContactsTable(usersHelper)
        val result =
            mContactsTable.getContactData(userID = currentUser.currentUserID, userName = displayName)
        result?.let {
            if (result.size > 0)
                return false
        }


        return true
    }

    @Throws(IOException::class)
    fun setUp(context: Context = con, serverAuthCode: String): PeopleService {
        val httpTransport: HttpTransport = NetHttpTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()

        val redirectUrl = "urn:ietf:wg:oauth:2.0:oob"

        val token = GoogleAuthorizationCodeTokenRequest(
            httpTransport, jsonFactory, context.getString(R.string.webClientID),
            context.getString(R.string.webClientSecret), serverAuthCode, redirectUrl
        ).execute()

        val credential: GoogleCredential = GoogleCredential.Builder()
            .setClientSecrets(context.getString(R.string.webClientID), context.getString(R.string.webClientSecret))
            .setTransport(httpTransport)
            .setJsonFactory(jsonFactory)
            .build()
        credential.setFromTokenResponse(token)

        var peopleBuilder = PeopleService.Builder(httpTransport, jsonFactory, credential)
        peopleBuilder.applicationName = context.getString(R.string.app_name)
        val peopleService: PeopleService = peopleBuilder.build()
        return peopleService

    }




}