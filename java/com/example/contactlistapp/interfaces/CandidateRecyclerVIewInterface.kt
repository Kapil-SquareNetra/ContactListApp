package com.example.contactlistapp.interfaces

import com.example.contactlistapp.models.CandidateList

interface CandidateRecyclerVIewInterface {

    fun onItemClick(items: CandidateList, position: Int)


}