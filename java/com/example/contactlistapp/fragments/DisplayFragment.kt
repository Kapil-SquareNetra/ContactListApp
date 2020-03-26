package com.example.contactlistapp.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.contactlistapp.R
import com.example.contactlistapp.models.Contacts
import com.example.contactlistapp.models.Supplier
import kotlinx.android.synthetic.main.fragment_display.*


class DisplayFragment: Fragment() {
   val  PERMISSION_REQUEST_CALL=3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition= TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_right)
        enterTransition=transition
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_display,container,false)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var bundle= arguments

        var position: Int= bundle!!.getInt("clicked_position")
        setDisplayData(position)

        imgCall.setOnClickListener {
            requestCallPermissions()
        }
    }

    fun requestCallPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val galleryPermissionStatus = ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.CALL_PHONE
            )
            if (galleryPermissionStatus != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Denied!!!!")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    PERMISSION_REQUEST_CALL
                )
            } else {
                makePhoneCall()
            }
        } else {
            makePhoneCall()
        }

    }

    fun makePhoneCall(){
        try {
            var phoneNumber= txtPhoneNumber.text.toString().replace("\\s","")
            phoneNumber.replace("-","")
            val dial= "tel:$phoneNumber"
            val callIntent= Intent()
            callIntent.setData(Uri.parse(dial))
            callIntent.action= Intent.ACTION_CALL
            startActivity(callIntent)
        }
        catch (e:Exception){
            Toast.makeText(requireActivity(),"Call Failed", Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CALL -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Denied by user")
                } else {
                    Log.d("Permission", "Granted By User")
                    makePhoneCall()
                }

            }
        }
    }

    fun setDisplayData(position: Int){

        var currContact: Contacts= Supplier.displayItems[position]

        currContact.name?.let {
            cltBar.title=currContact.name.toString()
        }

        currContact.phoneMap?.let {

            for (tag in currContact.phoneMap!!.keys){
                txtPhoneTitle.text= tag
                txtPhoneNumber.text=currContact.phoneMap!!.get(tag).toString()
                layoutPhone.visibility=View.VISIBLE
                break
            }
        }

        currContact.emailMap?.let {

            for (tag in currContact.emailMap!!.keys){
                txtEmailTitle.text= tag
                txtEmailID.text=currContact.emailMap!!.get(tag).toString()
                layoutEmail.visibility=View.VISIBLE
                break
            }
        }

        currContact.addressMap?.let {

            for (tag in currContact.addressMap!!.keys){
                txtAddressTitle.text= tag
                txtAddress.text=currContact.addressMap!!.get(tag).toString()
                layoutAddress.visibility=View.VISIBLE
                break
            }
        }

        currContact.imMap?.let {

            for (tag in currContact.imMap!!.keys){
                txtIMTitle.text= tag
                txtIMID.text=currContact.imMap!!.get(tag).toString()
                layoutIM.visibility=View.VISIBLE
                break
            }
        }

        currContact.importantDatesMap?.let {

            for (tag in currContact.importantDatesMap!!.keys){
                txtImpDatesTitle.text= tag
                txtDates.text=currContact.importantDatesMap!!.get(tag).toString()
                layoutImpDates.visibility=View.VISIBLE
                break
            }
        }

        currContact.relationshipMap?.let {

            for (tag in currContact.relationshipMap!!.keys){
                txtCircleTitle.text= tag
                txtCircleName.text=currContact.relationshipMap!!.get(tag).toString()
                layoutCircle.visibility=View.VISIBLE
                break
            }
        }

        currContact.phoneticName?.let {
            txtDispPhoneticName.text= currContact.phoneticName.toString()
            layoutPhoneticName.visibility=View.VISIBLE
        }
        currContact.nickName?.let {
            txtDispNickName.text= currContact.nickName.toString()
            layoutNickName.visibility=View.VISIBLE
        }
        currContact.company?.let {
            txtDispCompany.text= currContact.company.toString()
            layoutCompany.visibility=View.VISIBLE
        }
        currContact.Title?.let {
            txtDispTitle.text= currContact.Title.toString()
            layoutTitle.visibility=View.VISIBLE
        }
        currContact.website?.let {
            txtDispWebsite.text= currContact.website.toString()
            layoutWebsite.visibility=View.VISIBLE
        }
        currContact.notes?.let {
            txtDispNotes.text= currContact.notes.toString()
            layoutNotes.visibility=View.VISIBLE
        }
        currContact.group?.let {
            txtGroupName.text= currContact.group.toString()
            layoutGroup.visibility=View.VISIBLE
        }

       imgDisp.setImageResource(currContact.imgSource)






    }

}