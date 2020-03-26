package com.example.contactlistapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistapp.R
import com.example.contactlistapp.interfaces.RecyclerViewClickInterface
import com.example.contactlistapp.models.Contacts
import kotlinx.android.synthetic.main.contact_item.view.*


class ContactAdapter(val contacts: List<Contacts>, var clickInterface: RecyclerViewClickInterface): RecyclerView.Adapter<myViewHolder>(){

    //var searchList: List<Contacts>?= contacts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.contact_item,parent,false)
        return  myViewHolder(view)
    }

    override fun getItemCount(): Int {
    return contacts.size
     }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
    val items= contacts[position]

        holder.setData(items,clickInterface )

    }

    /*override fun getFilter(): Filter {
        return exampleFilter
    }*/




}

class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


    fun setData(item: Contacts?, action:RecyclerViewClickInterface){
        val name=item!!.name
        val initial= name?.toCharArray()
        itemView.txtContactName.text= name
        itemView.txtInitial.text= initial?.get(0).toString()
        itemView.imgContact.setImageResource(item.imgSource)
        itemView.imgContact.transitionName=item!!.name

        itemView.setOnClickListener { action.onItemClick(item, adapterPosition) }
        //itemView.imgCall.setOnClickListener { action.onCallClick(item, adapterPosition)  }

    }


}



