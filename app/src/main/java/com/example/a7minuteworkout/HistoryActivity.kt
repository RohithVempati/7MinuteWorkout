package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.a7minuteworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding:ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarHistoryActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Workout History"
        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val dao = (application as WorkoutApp).db.historyDao()
        getAllDates(dao)
    }



    private fun getAllDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect{
                if(it.isNotEmpty()){
                    binding?.rvHistory?.visibility=View.VISIBLE
                    binding?.noData?.visibility=View.GONE
                    val historyList = ArrayList<String>()
                    for(i in it){
                        historyList.add(i.date)
                    }
                    val historyAdapter = HistoryAdapter(historyList)
                    binding?.rvHistory?.adapter = historyAdapter

                }
                else{
                    binding?.rvHistory?.visibility=View.GONE
                    binding?.noData?.visibility=View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}