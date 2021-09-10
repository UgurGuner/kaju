package com.example.kaju

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.bold
import com.google.firebase.auth.FirebaseAuth
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class WalkingTrackActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking_track)

        firebaseAuth = FirebaseAuth.getInstance()
        var currentUser = firebaseAuth.currentUser

        if(currentUser==null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        val currentNumberOfStepsPercentage = 8000/100
        val whatsappButton = findViewById<AppCompatButton>(R.id.whatsappButton)
        val progressCircular = findViewById<CircularProgressBar>(R.id.circularProgressBar)
        val circularBarPercentage = findViewById<TextView>(R.id.circularProgressBarPercentage)

        circularBarPercentage.text = "%$currentNumberOfStepsPercentage"

        progressCircular.apply {
            setProgressWithAnimation(currentNumberOfStepsPercentage.toFloat(),1000)
        }

        val walkingTrackInfo = findViewById<TextView>(R.id.walkingInfoUp)
        val wInfo = "Günlük hedefin: "
        val boldInfo = "10.000 adım. \n"
        val restInfo = "Yürümek hem kalbin hem de ruhun için çok faydalı.\nYürüyüş esnasında dinlenmek için kendine güzel bir playlist hazırlamayı unutma :)"
        val customWalkingTrackInfoUp = SpannableStringBuilder()
            .append(wInfo)
            .bold { append(boldInfo) }
            .append(restInfo)
        walkingTrackInfo.text = customWalkingTrackInfoUp

        whatsappButton.setOnClickListener{
            sendMessage(message = "hello")
        }
        val profileIconButton = findViewById<Button>(R.id.profileButton)
        profileIconButton.setOnClickListener{

            startActivity(Intent(this,ProfileActivity::class.java))

        }

        val homeScreenButton = findViewById<Button>(R.id.homeScreenButton)
        homeScreenButton.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    private fun sendMessage(message: String)
    {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra(Intent.EXTRA_TEXT,message)
        if (intent.resolveActivity(packageManager) == null) {
            Toast.makeText(this,
                "Please install whatsapp first.",
                Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(intent)

    }

}