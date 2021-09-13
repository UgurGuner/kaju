package com.example.kaju

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class FoodTrackActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager?=null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>?=null
    private lateinit var firebaseAuth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_track)

        firebaseAuth = FirebaseAuth.getInstance()
        var currentUser = firebaseAuth.currentUser

        if(currentUser==null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        val openPopUp = findViewById<AppCompatButton>(R.id.openPopUp)
        val upssLayout = findViewById<LinearLayout>(R.id.upssLayout)
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



        val profileIconButton = findViewById<Button>(R.id.profileButton)
        profileIconButton.setOnClickListener{

            startActivity(Intent(this,ProfileActivity::class.java))

        }
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager

        adapter = RecyclerAdapter()
        recyclerView.adapter = adapter

        val whatsappButton = findViewById<Button>(R.id.whatsappButton)
        whatsappButton.setOnClickListener{
            sendMessage(message = "hello")
        }

        val homeScreenButton = findViewById<Button>(R.id.homeScreenButton)
        homeScreenButton.setOnClickListener{
            startActivity(Intent(this,MainMenu::class.java))
            finish()
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
            val className = "FoodTrackActivity::class.java"
            val intent = Intent(this,CalendarView::class.java)
            intent.putExtra("className",className)
            startActivity(intent)

        }

        //val foodTrackerMain = findViewById<LinearLayout>(R.id.foodTrackerMain)
        val cheatFoodInfo = mutableListOf<String>()
        upssLayout.setOnClickListener{
            openPopUp.setBackgroundResource(R.mipmap.upwhitearrow)
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.layout_popup,null)
            window.contentView = view
            val cheatFood = view.findViewById<EditText>(R.id.cheatFoodInfo)
            val addButton = view.findViewById<AppCompatButton>(R.id.addButton)
            addButton.setOnClickListener{
                openPopUp.setBackgroundResource(R.mipmap.belowwhitearrow)
                 cheatFoodInfo.add(cheatFood.text.toString())
                window.dismiss()
            }
            window.isFocusable = true
            window.width = 800
            window.background
            window.animationStyle
            window.showAsDropDown(upssLayout,0,-700)

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