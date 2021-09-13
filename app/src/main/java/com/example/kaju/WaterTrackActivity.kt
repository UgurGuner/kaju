package com.example.kaju

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class WaterTrackActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager?=null
    private var layoutManager2: RecyclerView.LayoutManager?=null
    private var layoutManager3: RecyclerView.LayoutManager?=null
    private var layoutManager4: RecyclerView.LayoutManager?=null
    private var adapter: RecyclerView.Adapter<WaterRecyclerAdapter.ViewHolder>?=null
    private var adapter2: RecyclerView.Adapter<WaterRecyclerAdapter.ViewHolder>?=null
    private var adapter3: RecyclerView.Adapter<WaterRecyclerAdapter.ViewHolder>?=null
    private var adapter4: RecyclerView.Adapter<WaterRecyclerAdapter.ViewHolder>?=null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_track)

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



        val waterTrackInfoUp = findViewById<TextView>(R.id.waterInfoUp)
        val wInfo = "Günlük tüketmen gereken su miktarı "
        val boldInfo = "3.5 litre. "
        val restInfo = "Unutma ki, su vücudumuzun en önemli takviyesi. Lütfen kendini susuz bırakma :)"
        val customWaterTrackInfoUp = SpannableStringBuilder()
            .append(wInfo)
            .bold { append(boldInfo) }
            .append(restInfo)
        waterTrackInfoUp.text = customWaterTrackInfoUp

        val whatsappButton = findViewById<Button>(R.id.whatsappButton)
        val recyclerView = findViewById<RecyclerView>(R.id.firstLine)
        val recyclerView2 = findViewById<RecyclerView>(R.id.secondLine)
        val recyclerView3 = findViewById<RecyclerView>(R.id.thirdLine)
        val recyclerView4 = findViewById<RecyclerView>(R.id.fourthLine)
        layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        layoutManager2 = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        layoutManager3 = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        layoutManager4 = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)


        recyclerView.layoutManager = layoutManager
        recyclerView2.layoutManager = layoutManager2
        recyclerView3.layoutManager = layoutManager3
        recyclerView4.layoutManager = layoutManager4

        adapter = WaterRecyclerAdapter()
        adapter2 = WaterRecyclerAdapter()
        adapter3 = WaterRecyclerAdapter()
        adapter4 = WaterRecyclerAdapter()

        recyclerView.adapter = adapter
        recyclerView2.adapter = adapter2
        recyclerView3.adapter = adapter3
        recyclerView4.adapter = adapter4


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
            val className = "WaterTrackActivity::class.java"
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