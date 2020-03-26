package com.example.contactlistapp.interfaces

import com.example.contactlistapp.models.CandidateList
import com.example.contactlistapp.models.Contacts

interface RecyclerViewClickInterface {

    fun onItemClick(items: Contacts, position: Int)


    //fun onCallClick(items: Contacts, position: Int)

}