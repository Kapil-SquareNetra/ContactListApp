package com.example.contactlistapp.fragments

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistapp.AsyncTasks.ImportDataTask
import com.example.contactlistapp.R
import com.example.contactlistapp.adapters.ContactAdapter
import com.example.contactlistapp.interfaces.RecyclerViewClickInterface
import com.example.contactlistapp.models.Contacts
import com.example.contactlistapp.models.Supplier.displayItems
import com.example.contactlistapp.models.Supplier.items
import kotlinx.android.synthetic.main.search_activity.*


const val PERMISSION_REQUEST = 77

class SearchFragment : Fragment(), RecyclerViewClickInterface {
    lateinit var displayList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        enterTransition = transition

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_activity, container, false)
        val tbSearch = view.findViewById<Toolbar>(R.id.tbSearch)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tbSearch.inflateMenu(R.menu.search_menu)
            val searchItem = tbSearch.menu.findItem(R.id.itemSearch)
            if (searchItem != null) {
                val searchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText!!.isNotEmpty()) {
                            displayItems.clear()
                            val search = newText.toLowerCase()
                            items.forEach {
                                val item = it
                                val subNames = it.name?.toString()?.split("\\s".toRegex())
                                subNames?.forEach {
                                    if (it.toLowerCase().startsWith(search, true)) {
                                        displayItems.add(item)
                                    }
                                }
                            }
                            recyclerView.adapter?.notifyDataSetChanged()
                        } else {
                            displayItems.clear()
                            displayItems.addAll(items)
                            recyclerView.adapter?.notifyDataSetChanged()
                        }

                        return true
                    }

                })
            }

        }
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            val task= ImportDataTask(this)
            task.execute("name")

        }
        catch (e: Exception){
            Log.e("MainScreen Exception", e.message)
        }
        displayList = requireActivity().findViewById(R.id.recyclerView) as RecyclerView

        displayList.adapter = ContactAdapter(displayItems, this)

        fabAddContact.setOnClickListener {
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            val fragment = Contact_Fragment()
            transaction.replace(R.id.fragmentHolder, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }


    override fun onItemClick(item: Contacts, position: Int) {

        var bundle = Bundle()
        bundle.putInt("clicked_position", position)
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        val fragment = DisplayFragment()
        fragment.arguments = bundle
        transaction.replace(R.id.fragmentHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }

}