package com.example.contactlistapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistapp.R
import com.example.contactlistapp.interfaces.CandidateRecyclerVIewInterface
import com.example.contactlistapp.interfaces.RecyclerViewClickInterface
import com.example.contactlistapp.models.CandidateList
import kotlinx.android.synthetic.main.candidate_item.view.*

class TestAdapter(val candidates: List<CandidateList>, var myClickInterface: CandidateRecyclerVIewInterface ) : RecyclerView.Adapter<myHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.candidate_item, parent, false)
        return myHolder(view)
    }

    override fun getItemCount(): Int {
        return  candidates.size
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
      val items= candidates[position]
        holder.data(items, myClickInterface)
    }

}

class myHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun data(item: CandidateList?, action: CandidateRecyclerVIewInterface){
        val cName= item!!.candidateName
        val cEmail= item.candidateEmail

        itemView.txtCandidateName.text= cName
        itemView.txtCandidateEmail.text=cEmail
        itemView.setOnClickListener {
            action.onItemClick(item,adapterPosition)
        }
    }

}