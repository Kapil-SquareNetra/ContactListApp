package com.example.contactlistapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistapp.R
import com.example.contactlistapp.interfaces.CandidateRecyclerVIewInterface
import com.example.contactlistapp.models.CandidateList
import kotlinx.android.synthetic.main.candidate_item.view.*

class CandidateAdapter(val candidates: List<CandidateList>, var clickInterface: CandidateRecyclerVIewInterface): RecyclerView.Adapter<CandidateViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.candidate_item, parent,false)
        return CandidateViewHolder(view)
    }

    override fun getItemCount(): Int {
      return  candidates.size
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {

        val candidateItem= candidates[position]
        holder.setData(candidateItem,clickInterface)

    }

}

class CandidateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    fun setData(item: CandidateList?, action:CandidateRecyclerVIewInterface){

        val candidateName= item!!.candidateName
        val candidateEmail= item.candidateEmail
        itemView.txtCandidateName.text= candidateName
        itemView.txtCandidateEmail.text= candidateEmail
        itemView.setOnClickListener {
            action.onItemClick(item,adapterPosition)
        }
    }
}