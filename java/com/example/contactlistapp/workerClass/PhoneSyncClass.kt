package com.example.contactlistapp.workerClass

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contactlistapp.activities.currentUser
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.models.PhoneImportModel
import com.example.contactlistapp.tables.ContactsTable
import com.example.contactlistapp.tables.NumbersTable
import java.lang.Exception

class PhoneSyncClass(context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    val con= context



    override fun doWork(): Result {
    ImportFromPhone()
    return Result.success()
    }



    fun ImportFromPhone() {
        val usersHelper = UsersHelper(con)
        val mContactsTable = ContactsTable(usersHelper)
        val mNumbersTable = NumbersTable(usersHelper)


        var list: MutableList<PhoneImportModel> = mutableListOf()
        try {


            val contacts: Cursor? = con.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            contacts?.let {
                while (contacts.moveToNext()) {
                    val customLabel=contacts.getInt(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                    val label= contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL))
                    var phoneTypeLabel= ContactsContract.CommonDataKinds.Phone.getTypeLabel(con.resources,customLabel,label)

                    list.add(
                        PhoneImportModel(
                            contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                            phoneTypeLabel.toString(),
                            contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        )
                    )
                }
                contacts.close()
            }


            for (i in 0 until list.size) {
                val contactID = getcontactID()
                if (validateValues(list[i].displayName)){
                if (mContactsTable.insertContactData(
                        contactID = contactID,
                        userID = currentUser.currentUserID,
                        contactName = list[i].displayName
                    )
                )
                    if (mNumbersTable.insertNumberData(
                            contactID = contactID,
                            numType = list[i].type,
                            number = list[i].Number
                        )
                    )
                    else
                        return
                }
            }
            Toast.makeText(con, "Contacts added", Toast.LENGTH_SHORT).show()

            /* val task= ImportDataTask(MainScreen())
                 task.execute()*/
        } catch (e: Exception) {
            println("error at ImportFromPhone: ${e.message}")
        }

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

    fun getcontactID(): Int {
        var usersHelper = UsersHelper(con)
        var mContactsTable = ContactsTable(usersHelper)
        val idResult = mContactsTable.getContactsID()
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





}

