package com.example.contactlistapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.contactlistapp.R
import com.example.contactlistapp.adapters.TestAdapter
import com.example.contactlistapp.interfaces.CandidateRecyclerVIewInterface
import com.example.contactlistapp.models.CandidateList
import com.example.contactlistapp.models.candidateSupplier
import com.example.contactlistapp.workerClass.CandidateImportWorker
import kotlinx.android.synthetic.main.activity_candidate.*

class CandidateActivity : AppCompatActivity(), CandidateRecyclerVIewInterface {

    val DISPLAY_CANDIDATE_REQUEST=5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        populateCandidateTable(this,this)
    }


    fun populateCandidateTable(context: Context, owner: LifecycleOwner){

        try {
            var populateRequest: OneTimeWorkRequest?= OneTimeWorkRequest.Builder(CandidateImportWorker::class.java).build()
            populateRequest?.let {
                layoutCandidatesLoadProgress.visibility=View.VISIBLE
                WorkManager.getInstance(context).enqueue(populateRequest)

                WorkManager.getInstance(context).getWorkInfoByIdLiveData(populateRequest.id).observe(owner,
                    Observer {
                        if (it.state.isFinished){
                            val layoutManager= LinearLayoutManager(this)
                            layoutManager.orientation= LinearLayoutManager.VERTICAL
                            candidateRecyclerView.layoutManager=layoutManager
                            candidateSupplier.candidateItems.sortBy { it.candidateName }
                            candidateRecyclerView.adapter=TestAdapter(candidateSupplier.candidateItems,this)
                            layoutCandidatesLoadProgress.visibility=View.GONE

                        }
                    })





            }
        }catch (e:Exception){
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
        }





    }




    override fun onItemClick(items: CandidateList, position: Int) {
        val intent= Intent(this, DisplayCandidateActivity::class.java )
        intent.putExtra("clicked_position",position)
        startActivityForResult(intent,DISPLAY_CANDIDATE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
