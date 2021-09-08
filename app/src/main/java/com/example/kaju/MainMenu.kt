package com.example.kaju

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class MainMenu : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        firebaseAuth = FirebaseAuth.getInstance()
        var currentUser = firebaseAuth.currentUser

        val hello = "Hello"
        val homeScreenButton = findViewById<Button>(R.id.homeScreenButton)
        val profileIconButton = findViewById<Button>(R.id.profileButton)
        val whatsappButton = findViewById<Button>(R.id.whatsappButton)
        val foodTrack = findViewById<Button>(R.id.foodTrackButton)
        val waterTrack = findViewById<Button>(R.id.waterButton)
        val walkingTrack = findViewById<Button>(R.id.walkingButton)
        val plusButton = findViewById<Button>(R.id.plusButton)

        if(currentUser==null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        homeScreenButton.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        profileIconButton.setOnClickListener{

            startActivity(Intent(this,ProfileActivity::class.java))

        }

        whatsappButton.setOnClickListener{

            sendMessage(hello)
        }
        foodTrack.setOnClickListener{
            startActivity(Intent(this,FoodTrackActivity::class.java))
        }
        waterTrack.setOnClickListener{
            startActivity(Intent(this,WaterTrackActivity::class.java))
        }

        walkingTrack.setOnClickListener{
            startActivity(Intent(this,WalkingTrackActivity::class.java))
        }

        plusButton.setOnClickListener{

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