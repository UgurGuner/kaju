package com.example.kaju

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.bold
import com.google.firebase.auth.FirebaseAuth
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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

        val leftArrow = findViewById<AppCompatButton>(R.id.decreaseDay)
        val rightArrow = findViewById<AppCompatButton>(R.id.increaseDay)
        val calendarDate = findViewById<TextView>(R.id.calendarDay)

        var intentFromMainMenu = intent.getStringExtra("dateMainMenu")

        var intentComing = intent.getStringExtra("date")

        if(!intentComing.isNullOrEmpty())
        {
            calendarDate.text = intentComing
        }
        if(!intentFromMainMenu.isNullOrEmpty())
        {
            calendarDate.text = intentFromMainMenu
        }


        leftArrow.setOnClickListener{

            if(!intentComing.isNullOrEmpty())
            {
                calendarDate.text = intentComing
                val simpleDate = SimpleDateFormat("dd/MM/yyyy")
                val c = Calendar.getInstance()
                try {
                    c.time = simpleDate.parse(intentComing)

                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }

                c.add(Calendar.DATE,-1)
                val newSimpleDate = SimpleDateFormat("dd/MM/yyyy")
                intentComing = newSimpleDate.format(c.time)

                calendarDate.text = intentComing
            }
            if(!intentFromMainMenu.isNullOrEmpty())
            {
                calendarDate.text = intentFromMainMenu
                val simpleDate = SimpleDateFormat("dd/MM/yyyy")
                val c = Calendar.getInstance()
                try {
                    c.time = simpleDate.parse(intentFromMainMenu)

                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }

                c.add(Calendar.DATE,-1)
                val newSimpleDate = SimpleDateFormat("dd/MM/yyyy")
                intentFromMainMenu = newSimpleDate.format(c.time)

                calendarDate.text = intentFromMainMenu
            }

        }

        rightArrow.setOnClickListener{
            if(!intentComing.isNullOrEmpty())
            {
                calendarDate.text = intentComing
                val simpleDate = SimpleDateFormat("dd/MM/yyyy")
                val c = Calendar.getInstance()
                try {
                    c.time = simpleDate.parse(intentComing)

                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }

                c.add(Calendar.DATE,1)
                val newSimpleDate = SimpleDateFormat("dd/MM/yyyy")
                intentComing = newSimpleDate.format(c.time)

                calendarDate.text = intentComing
            }
            if(!intentFromMainMenu.isNullOrEmpty())
            {
                calendarDate.text = intentFromMainMenu
                val simpleDate = SimpleDateFormat("dd/MM/yyyy")
                val c = Calendar.getInstance()
                try {
                    c.time = simpleDate.parse(intentFromMainMenu)

                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }

                c.add(Calendar.DATE,1)
                val newSimpleDate = SimpleDateFormat("dd/MM/yyyy")
                intentFromMainMenu = newSimpleDate.format(c.time)

                calendarDate.text = intentFromMainMenu
            }

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
            startActivity(Intent(this,MainMenu::class.java))
            finish()
        }

        val calendarButton = findViewById<ImageView>(R.id.calendarButton)
        calendarButton.setOnClickListener{
            val className = "WalkingTrackActivity::class.java"
            val intent = Intent(this,CalendarView::class.java)
            intent.putExtra("className",className)
            startActivity(intent)

        }

        val plusButton = findViewById<Button>(R.id.plusButton)
        plusButton.setOnClickListener{
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.layout_pluspopup,null)
            window.contentView = view
            window.isFocusable = true
            window.width = 1000
            window.background
            window.animationStyle
            window.showAsDropDown(plusButton,-416,-350)

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