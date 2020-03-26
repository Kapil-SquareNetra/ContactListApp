package com.example.contactlistapp.models

import com.example.contactlistapp.R


data class Contacts(
    var name: String? = null,
    var phoneticName: String? = null,
    var nickName: String? = null,
    var company: String? = null,
    var Title: String? = null,
    var phoneMap: HashMap<String, String>? = null,
    var emailMap: HashMap<String, String>? = null,
    var addressMap: HashMap<String, String>? = null,
    var imMap: HashMap<String, String>? = null,
    var website: String? = null,
    var importantDatesMap: HashMap<String, String>? = null,
    var relationshipMap: HashMap<String, String>? = null,
    var notes: String? = null,
    var group: String? = null,
    var imgSource: Int=R.drawable.ic_person
)

object Supplier {

     var items: MutableList<Contacts> = mutableListOf()
    var displayItems: MutableList<Contacts> = mutableListOf()
   /* var items = mutableListOf(
        Contacts("Kapil", phoneMap = hashMapOf("Mobile" to 8879772063L), imgSource = R.drawable.boy001)
        )*/
}
