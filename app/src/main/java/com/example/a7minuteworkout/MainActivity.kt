package com.example.a7minuteworkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a7minuteworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        binding?.start?.setOnClickListener {
            startActivity(Intent(this,ExerciseActivity::class.java))
        }
        binding?.bmiCalc?.setOnClickListener{
            startActivity(Intent(this,BMIActivity::class.java))
        }
        binding?.btnHistory?.setOnClickListener{
            startActivity(Intent(this,HistoryActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}