package com.example.kaju

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainMenu : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        firebaseAnalytics = Firebase.analytics
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser




        if(currentUser==null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        val hello = "Hello"
        val homeScreenButton = findViewById<Button>(R.id.homeScreenButton)
        val profileIconButton = findViewById<Button>(R.id.profileButton)
        val whatsappButton = findViewById<Button>(R.id.whatsappButton)
        val foodTrack = findViewById<Button>(R.id.foodTrackButton)
        val waterTrack = findViewById<Button>(R.id.waterButton)
        val walkingTrack = findViewById<Button>(R.id.walkingButton)
        val leftArrow = findViewById<AppCompatButton>(R.id.decreaseDay)
        val rightArrow = findViewById<AppCompatButton>(R.id.increaseDay)
        val calendarDate = findViewById<TextView>(R.id.calendarDay)

        var intentComing = intent.getStringExtra("date")
        if(!intentComing.isNullOrEmpty())
        {
            calendarDate.text = intentComing
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


        }



        homeScreenButton.setOnClickListener{
            startActivity(Intent(this,MainMenu::class.java))
            finish()
        }

        profileIconButton.setOnClickListener{

            startActivity(Intent(this,ProfileActivity::class.java))

        }

        whatsappButton.setOnClickListener{

            sendMessage(hello)
        }
        foodTrack.setOnClickListener{
            val newIntent = Intent(this,FoodTrackActivity::class.java)
            newIntent.putExtra("dateMainMenu",intentComing)
            startActivity(newIntent)
        }
        waterTrack.setOnClickListener{
            val newIntent = Intent(this,WaterTrackActivity::class.java)
            newIntent.putExtra("dateMainMenu",intentComing)
            startActivity(newIntent)
        }

        walkingTrack.setOnClickListener{
            val newIntent = Intent(this,WalkingTrackActivity::class.java)
            newIntent.putExtra("dateMainMenu",intentComing)
            startActivity(newIntent)
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

        val calendarButton = findViewById<ImageView>(R.id.calendarButton)
        calendarButton.setOnClickListener{
            val className = "MainMenu::class.java"
            val intent = Intent(this,CalendarView::class.java)
            intent.putExtra("className",className)
            startActivity(intent)

        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_NAME, intentComing.toString())
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
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