package com.example.kaju

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView

class CalendarView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        val intentComing = intent.getStringExtra("className")


        calendarView?.setOnDateChangeListener{ _, year, month, day ->

           if(month<10)
           {
               if(day<10)
               {
                   val selectedDate ="0" + day + "/0" + (month+1) + "/" + year
                   when {
                       intentComing.equals("MainMenu::class.java") -> {
                           val intent = Intent(this,MainMenu::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WalkingTrackActivity::class.java") -> {
                           val intent = Intent(this,WalkingTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WaterTrackActivity::class.java") -> {
                           val intent = Intent(this,WaterTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("FoodTrackActivity::class.java") -> {
                           val intent = Intent(this,FoodTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                   }
               }
               else
               {
                   val selectedDate ="" + day + "/0" + (month+1) + "/" + year
                   when {
                       intentComing.equals("MainMenu::class.java") -> {
                           val intent = Intent(this,MainMenu::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WalkingTrackActivity::class.java") -> {
                           val intent = Intent(this,WalkingTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WaterTrackActivity::class.java") -> {
                           val intent = Intent(this,WaterTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("FoodTrackActivity::class.java") -> {
                           val intent = Intent(this,FoodTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                   }
               }
           }
            else
           {
               if(day<10)
               {
                   val selectedDate ="0" + day + "/0" + (month+1) + "/" + year
                   when {
                       intentComing.equals("MainMenu::class.java") -> {
                           val intent = Intent(this,MainMenu::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WalkingTrackActivity::class.java") -> {
                           val intent = Intent(this,WalkingTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WaterTrackActivity::class.java") -> {
                           val intent = Intent(this,WaterTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("FoodTrackActivity::class.java") -> {
                           val intent = Intent(this,FoodTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                   }
               }
               else
               {
                   val selectedDate ="" + day + "/0" + (month+1) + "/" + year
                   when {
                       intentComing.equals("MainMenu::class.java") -> {
                           val intent = Intent(this,MainMenu::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WalkingTrackActivity::class.java") -> {
                           val intent = Intent(this,WalkingTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("WaterTrackActivity::class.java") -> {
                           val intent = Intent(this,WaterTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                       intentComing.equals("FoodTrackActivity::class.java") -> {
                           val intent = Intent(this,FoodTrackActivity::class.java)
                           intent.putExtra("date",selectedDate)
                           startActivity(intent)
                           finish()
                       }
                   }
               }
           }




        }


    }
}