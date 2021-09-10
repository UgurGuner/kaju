package com.example.kaju

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class FoodTrackActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager?=null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>?=null
    private lateinit var firebaseAuth: FirebaseAuth


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