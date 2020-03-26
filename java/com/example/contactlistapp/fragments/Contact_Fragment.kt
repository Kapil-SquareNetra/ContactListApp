package com.example.contactlistapp.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.contactlistapp.R
import com.example.contactlistapp.activities.currentUser
import com.example.contactlistapp.databaseHelpers.UsersHelper
import com.example.contactlistapp.models.ContactDetails
import com.example.contactlistapp.tables.*
import kotlinx.android.synthetic.main.avatar_chooser_activity.view.*
import kotlinx.android.synthetic.main.fragment_main.*


class Contact_Fragment : Fragment() {


    var phoneTag = arrayOf("Mobile", "Work", "Home", "Main")
    var emailTag = arrayOf("Home", "Work", "Other")
    var addressTag = arrayOf("Home", "Work", "Other")
    var imTag = arrayOf("Hangout", "Skype", "Windows Live", "Jabber", "Other")
    var bDayTag = arrayOf("Birthday", "Anniversary", "Other")
    var circleTag = arrayOf("Assistant", "Brother", "Child", "Father", "Friend", "Manager", "Other")
    var groupTag = arrayOf("Friends", "Family", "CoWorkers", "Other")
    val REQUEST_CODE_CONST = 1
    val CAMERA_REQUEST = 2
    val GALLERY_REQUEST = 3
    val PERMISSION_REQUEST_STORAGE = 1000
    val PERMISSION_REQUEST_CAMERA = 2000
    var avatarImg: Int = R.drawable.ic_person
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.slide_right)
        enterTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        val tbSave = rootView.findViewById<Toolbar>(R.id.tbSave)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tbSave.inflateMenu(R.menu.contacts_menu)
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val bundle = arguments
        try {
            bundle?.getString("PHNUM")?.let {
                val txt = it.replace("\\s".toRegex(), "")
                edtPhone.setText(txt)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }


        val pT = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_style, phoneTag
        )
        spnPhone.adapter = pT

        val eT = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_style, emailTag
        )
        spnEmail.adapter = eT

        val aT = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_style, addressTag
        )
        spnAddress.adapter = aT


        val imT = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_style, imTag
        )
        spnIM.adapter = imT

        val bdT = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_style, bDayTag
        )
        spnBday.adapter = bdT

        val cT = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_style, circleTag
        )
        spnCircle.adapter = cT

        val gT = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_style, groupTag
        )
        spnGroup.adapter = gT

        Glide.with(requireActivity())
            .load(R.drawable.ic_person)
            .circleCrop()
            .into(imgAvatar)


        fabPhoto.setOnClickListener {
            openImportDialog()

        }


        tbSave.setOnMenuItemClickListener {
            if (it.itemId == R.id.itemSave) {
                if (validateContact() != false) {
                    saveContactToDB()
                    Toast.makeText(requireActivity(), "Contact Saved!!", Toast.LENGTH_LONG).show()
                    fragmentManager?.popBackStackImmediate()
                }
            }
            true
        }

    }


    fun openImportDialog() {

        val mDialogView =
            LayoutInflater.from(requireActivity()).inflate(R.layout.avatar_chooser_activity, null)
        val builder = AlertDialog.Builder(requireActivity()).setView(mDialogView).create()
        builder.show()
        mDialogView.btnCamera.setOnClickListener {
            requestCameraPermission()
            builder.dismiss()
        }

        mDialogView.btnGallery.setOnClickListener {
            requestGalleryPermission()
            builder.dismiss()

        }

        mDialogView.btnCancelImport.setOnClickListener {
            builder.dismiss()
        }
    }

    fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    fun pickImageFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Denied by user")
                } else {
                    Log.d("Permission", "Granted By User")
                    pickImageFromGallery()
                }

            }
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Denied by user")
                } else {
                    Log.d("Permission", "Granted By User")
                    pickImageFromCamera()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CONST) {
            if (resultCode == RESULT_OK) {
                avatarImg = data!!.getIntExtra("imgAddr", R.drawable.ic_person)
                imgAvatar.setImageResource(avatarImg)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "No Avatar selected", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                val imageUri: Uri? = data?.data
                val imageStream= requireActivity().contentResolver.openInputStream(imageUri!!)
                val imageBitmap= BitmapFactory.decodeStream(imageStream)
                imageBitmap?.let {
                    Glide.with(requireActivity()).load(imageBitmap).circleCrop().into(imgAvatar)
                    //imgAvatar.setImageURI(imageUri)
                    Toast.makeText(requireActivity(), "Image added", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    requireContext(),
                    "No Image selected from Gallery",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                imageUri?.let {
                    val imageStream= requireActivity().contentResolver.openInputStream(imageUri!!)
                    val imageBitmap= BitmapFactory.decodeStream(imageStream)
                    Glide.with(requireActivity()).load(imageBitmap).circleCrop().into(imgAvatar)
                }

            }
        }
        }

        fun requestCameraPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val writeStoragePermission = ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                val cameraPermission = ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.CAMERA
                )
                if (writeStoragePermission != PackageManager.PERMISSION_GRANTED
                    || cameraPermission != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA
                        ),
                        PERMISSION_REQUEST_CAMERA
                    )
                } else {
                    pickImageFromCamera()
                }

            } else {
                pickImageFromCamera()
            }

        }

        fun requestGalleryPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val galleryPermissionStatus = ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (galleryPermissionStatus != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions", "Denied!!!!")
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_STORAGE
                    )
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }

        }

        fun getcontactID(): Int {
            var usersHelper = UsersHelper(requireActivity())
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
            return 1
        }

        fun saveContactToDB() {

            try {

                var usersHelper = UsersHelper(requireActivity())
                var mContactsTable = ContactsTable(usersHelper)
                var mNumbersTable = NumbersTable(usersHelper)
                var mEmailsTable = EmailsTable(usersHelper)
                var mAddressTable = AddressTable(usersHelper)
                var mIMTable = IMTable(usersHelper)
                var mIMPDatesTable = IMPDatesTable(usersHelper)
                var mCircleTable = CircleTable(usersHelper)

                val contactID = getcontactID()
                var contactName: String
                var phoneticName: String? = null
                var nickName: String? = null
                var companyName: String? = null
                var title: String? = null
                var website: String? = null
                var notes: String? = null
                var group: String? = null



                contactName = edtName.text.toString().trim()

                if (edtPhoneticName.text.toString().trim().length > 0) phoneticName =
                    edtPhoneticName.text.toString().trim()

                if (edtNickName.text.toString().trim().length > 0) nickName =
                    edtNickName.text.toString().trim()

                if (edtCompany.text.toString().trim().length > 0) companyName =
                    edtCompany.text.toString()

                if (edtTitle.text.toString().trim().length > 0) title = edtTitle.text.toString()

                if (edtWebsite.text.toString().trim().length > 0) website =
                    edtWebsite.text.toString()

                if (edtNotes.text.toString().trim().length > 0) notes = edtNotes.text.toString()

                if (spnGroup.selectedItem.toString().trim().length > 0) group =
                    spnGroup.selectedItem.toString()

                mContactsTable.insertContactData(
                    contactID, currentUser.currentUserID, contactName,
                    nickName, phoneticName, companyName, title, website, notes, group
                )

                if (edtPhone.text.toString().trim().length > 0) {
                    var phoneTag: String = spnPhone.selectedItem.toString()
                    var phoneNumber: String = edtPhone.text.toString()
                    mNumbersTable.insertNumberData(contactID, phoneTag, phoneNumber)
                }

                if (edtEmail.text.toString().trim().length > 0) {
                    var emailTag: String = spnEmail.selectedItem.toString()
                    var email: String = edtEmail.text.toString()
                    mEmailsTable.insertEmailData(contactID, emailTag, email)

                }

                if (edtAddress.text.toString().trim().length > 0) {
                    var addressTag: String = spnAddress.selectedItem.toString()
                    var address: String = edtAddress.text.toString()
                    mAddressTable.insertAddressData(contactID, addressTag, address)
                }

                if (edtIM.text.toString().trim().length > 0) {
                    var imTag: String = spnIM.selectedItem.toString()
                    var im: String = edtIM.text.toString()
                    mIMTable.insertIMData(contactID, imTag, im)
                }


                if (edtBday.text.toString().trim().length > 0) {
                    var dateTag: String = spnBday.selectedItem.toString()
                    var date: String = edtBday.text.toString()
                    mIMPDatesTable.insertImpDatesData(contactID, dateTag, date)

                }

                if (edtCircle.text.toString().trim().length > 0) {
                    var circleTag: String = spnCircle.selectedItem.toString()
                    var circle: String = edtCircle.text.toString()
                    mCircleTable.insertCircleData(contactID, circleTag, circle)
                }
            } catch (e: Exception) {

                Log.e("saveToDBError", e.message)
            }


        }

        fun validateContact(): Boolean {
            var usersHelper = UsersHelper(requireActivity())
            var mContactsTable = ContactsTable(usersHelper)

            if (edtName.text.toString().equals("")) {
                Toast.makeText(requireActivity(), "Name is a mandatory field!", Toast.LENGTH_SHORT)
                    .show()
                return false
            }

            if (edtPhone.text.toString().equals("")) {
                Toast.makeText(
                    requireActivity(),
                    "Number is a mandatory field!",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            }

            val result: MutableList<ContactDetails>? =
                mContactsTable.getContactData(
                    userID = currentUser.currentUserID,
                    userName = edtName.text.toString()
                )
            result?.let {
                if (result.size > 0) {
                    Toast.makeText(
                        requireActivity(),
                        "Duplicate Contact cannot be created!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return false
                }
            }

            return true
        }


    }