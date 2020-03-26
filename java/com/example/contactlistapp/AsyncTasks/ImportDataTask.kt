package com.example.contactlistapp.AsyncTasks

import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistapp.R
import com.example.contactlistapp.activities.MainScreen
import com.example.contactlistapp.activities.currentUser
import com.example.contactlistapp.adapters.ContactAdapter
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.fragments.SearchFragment
import com.example.contactlistapp.models.Contacts
import com.example.contactlistapp.models.Supplier
import com.example.contactlistapp.models.ContactDetails
import com.example.contactlistapp.tables.*
import kotlinx.android.synthetic.main.main_screen.*
import kotlinx.android.synthetic.main.search_activity.*
import java.lang.ref.WeakReference

class ImportDataTask(context: SearchFragment) : AsyncTask<String?, String, String>() {
    var activityWeakReference: WeakReference<SearchFragment> = WeakReference(context)


    override fun onPreExecute() {
        super.onPreExecute()
        val con: SearchFragment? = activityWeakReference.get()
        if (con == null || con.isRemoving) return
        con.progressBar.visibility = View.VISIBLE
        //Toast.makeText(con.requireActivity(),"PreExecute", Toast.LENGTH_SHORT).show()
    }

    override fun doInBackground(vararg params: String?): String? {
        getAllContactsDataFromDB()
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        val con: SearchFragment? = activityWeakReference.get()
        if (con == null || con.isRemoving) return
        con.progressBar.visibility = View.GONE
        //Toast.makeText(con.requireActivity(),"PostExecute", Toast.LENGTH_SHORT).show()


        val layoutManager = LinearLayoutManager(con.requireActivity())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        con.displayList.layoutManager = layoutManager

        //getAllContactsDataFromDB()


        //Supplier.items.sortBy { it.name }
        Supplier.displayItems.clear()
        Supplier.displayItems.addAll(Supplier.items)
        Supplier.displayItems.sortBy { it.name }
        //con.displayList.adapter = ContactAdapter(Supplier.displayItems, this)
        con.recyclerView.adapter?.notifyDataSetChanged()

        if (Supplier.displayItems.size > 0) {
            con.txtNoContacts.visibility = View.INVISIBLE
        } else
            con.txtNoContacts.visibility = View.VISIBLE



        //con.showSearchFragment()
    }

    fun getAllContactsDataFromDB() {
        try {
            var usersHelper= UsersHelper(context = activityWeakReference.get()!!.requireActivity())
            var mContactsTable = ContactsTable(usersHelper)
            var mNumbersTable = NumbersTable(usersHelper)
            var mEmailsTable = EmailsTable(usersHelper)
            var mAddressTable = AddressTable(usersHelper)
            var mIMTable = IMTable(usersHelper)
            var mIMPDatesTable = IMPDatesTable(usersHelper)
            var mCircleTable = CircleTable(usersHelper)

            val con: SearchFragment? = activityWeakReference.get()
            if (con == null || con.isRemoving) return

            Supplier.items.clear()
            val result: MutableList<ContactDetails>? =
                mContactsTable.getContactData(userID = currentUser.currentUserID)
            result?.let {
                for (i in 0 until result.size) {
                    var currContact = Contacts()
                    currContact.name = result[i].contactName
                    currContact.nickName = result[i].nickName
                    currContact.phoneticName = result[i].phoneticName
                    currContact.website = result[i].website
                    currContact.notes = result[i].notes
                    currContact.group = result[i].group

                    var id = result[i].contactID
                    val numberResult = mNumbersTable.getNumberData(contactID = id.toInt())
                    numberResult?.let {
                        if (numberResult.size > 0) {
                            var phoneHash = hashMapOf<String, String>()
                            for (j in 0 until numberResult.size) {
                                phoneHash.put(numberResult[j].numType, numberResult[j].number)
                            }
                            currContact.phoneMap = phoneHash
                        }
                    }
                    val emailResult = mEmailsTable.getEmailData(contactID = id.toInt())
                    emailResult?.let {
                        if (emailResult.size > 0) {
                            var emailHash = hashMapOf<String, String>()
                            for (j in 0 until emailResult.size) {
                                emailHash.put(emailResult[j].emailType, emailResult[j].email)
                            }
                            currContact.emailMap = emailHash
                        }
                    }
                    val addressResult = mAddressTable.getAddressData(contactID = id.toInt())
                    addressResult?.let {
                        if (addressResult.size > 0) {
                            var addressHash = hashMapOf<String, String>()
                            for (j in 0 until addressResult.size) {
                                addressHash.put(
                                    addressResult[j].addressType,
                                    addressResult[j].address
                                )
                            }
                            currContact.addressMap = addressHash
                        }
                    }
                    val imResult = mIMTable.getIMData(contactID = id.toInt())
                    imResult?.let {
                        if (imResult.size > 0) {
                            var imHash = hashMapOf<String, String>()
                            for (j in 0 until imResult.size) {
                                imHash.put(imResult[j].imType, imResult[j].imAddr)
                            }
                            currContact.imMap = imHash
                        }
                    }
                    val impDatesResult = mIMPDatesTable.getIMPDatesData(contactID = id.toInt())
                    impDatesResult?.let {
                        if (impDatesResult.size > 0) {
                            var dateHash = hashMapOf<String, String>()
                            for (j in 0 until impDatesResult.size) {
                                dateHash.put(impDatesResult[j].dateType, impDatesResult[j].date)
                            }
                            currContact.importantDatesMap = dateHash
                        }
                    }
                    val circleResult = mCircleTable.getCircleData(contactID = id.toInt())
                    circleResult?.let {
                        if (circleResult.size > 0) {
                            var relationHash = hashMapOf<String, String>()
                            for (j in 0 until circleResult.size) {
                                relationHash.put(
                                    circleResult[j].circleType,
                                    circleResult[j].relation
                                )
                            }
                            currContact.addressMap = relationHash
                        }
                    }


                    Supplier.items.add(currContact)
                }
            }
            Log.i("Task Completed!", "Import Completed!")
        } catch (e: Exception) {
            Log.e("getDataError", e.message)
        }

    }


}