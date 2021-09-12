package com.example.kaju

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import org.w3c.dom.Text
import android.util.Pair as UtilPair

class MainActivity : AppCompatActivity() {

    lateinit var app_name : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app_name = findViewById<TextView>(R.id.app_name)



        val runnable = Runnable {
            val intent = Intent(this, EntranceActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this,app_name,ViewCompat.getTransitionName(app_name))
            startActivity(intent,options.toBundle())


        }

        Handler(Looper.getMainLooper()).postDelayed(runnable,2000)





    }


}