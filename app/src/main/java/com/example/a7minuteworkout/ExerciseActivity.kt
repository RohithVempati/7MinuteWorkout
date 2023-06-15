package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a7minuteworkout.databinding.ActivityExerciseBinding
import com.example.a7minuteworkout.databinding.DialogCustomBackConfirmationBinding

class ExerciseActivity : AppCompatActivity()  {

    private var tts:TextToSpeech?= null
    private var binding:ActivityExerciseBinding? =null
    private var restTimer:CountDownTimer?=null
    private var restProgress = 0
    private var exerciseTimer:CountDownTimer?=null
    private var exerciseProgress = 0
    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = 0
    private var player:MediaPlayer? = null
    private var exerciseAdapter:ExerciseStatusAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        tts = TextToSpeech(this,null)
        setContentView(binding?.root)
        speak("hello")
        speak("stay healthy!")

        setSupportActionBar(binding?.toolbar)

        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbar?.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        setRestProgressBar()
        setUpExerciseStatus()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun setRestProgressBar(){
        binding?.nextExerciseName?.visibility=View.VISIBLE
        binding?.nextExerciseName?.text=exerciseList!![currentExercisePosition].getName()
        binding?.exerciseName?.text = "GET READY FOR"
        binding?.ivExercise?.visibility = View.INVISIBLE
        binding?.timerlayoutExercise?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.timerlayout?.visibility = View.VISIBLE
        binding?.progressBar?.progress = restProgress
        speak("GET READY FOR")
        speak(exerciseList!![currentExercisePosition].getName())
        restTimer = object :CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress=10-restProgress
                binding?.timer?.text=(10-restProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition<exerciseList!!.size) {
                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseProgress = 0
                    restProgress =0
                    speak("Start")
                    setExerciseProgressBar()
                    setUpExerciseStatus()
                    speak(exerciseList!![currentExercisePosition].getName())
                    playMusic()
                }
                else{
                    Toast.makeText(this@ExerciseActivity, "Exercise Complete", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    private fun setExerciseProgressBar(){
        binding?.nextExerciseName?.visibility=View.GONE
        binding?.timerlayout?.visibility = View.GONE
        binding?.ivExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.ivExercise?.visibility = View.VISIBLE
        binding?.timerlayoutExercise?.visibility = View.VISIBLE
        binding?.exerciseName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object :CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress=30-exerciseProgress
                binding?.timerExercise?.text=(30-exerciseProgress).toString()
            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                currentExercisePosition++
                if(currentExercisePosition<exerciseList!!.size) {
                    exerciseProgress = 0
                    restProgress =0
                    player?.stop()
                    speak("Stop")
                    setRestProgressBar()
                    setUpExerciseStatus()
                }
                else{
                    player?.stop()
                    speak("Congratulations!! Your Workout is Complete")
                    startActivity(Intent(this@ExerciseActivity,FinishActivity::class.java))
                    finish()
                }
            }
        }.start()
    }

    private fun playMusic(){
        try{
            val SoundURI = Uri.parse("android.resource://com.example.a7minuteworkout/"+R.raw.bg_music)
            player = MediaPlayer.create(applicationContext,SoundURI)
            player?.isLooping = false
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun setUpExerciseStatus(){
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter=exerciseAdapter
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }


    fun speak(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_ADD,null,"")
    }


    override fun onDestroy() {
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(player!=null){
            player!!.stop()
        }
        super.onDestroy()
        binding = null
    }
}