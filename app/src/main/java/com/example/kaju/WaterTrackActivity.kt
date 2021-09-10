package com.example.kaju

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

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