package com.example.kaju

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Person
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.vanniktech.rxpermission.RealRxPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.StringBuilder
import java.nio.charset.Charset

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore
    private val notificationPerm = 101
    private val cameraPerm = 102
    private val Channel_ID = "Your_Channel_ID"
    private val birthdate = "Doğum Tarihi: "
    private val sex = "Cinsiyet: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val textDailyPlan = findViewById<TextView>(R.id.textDailyPlan)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val yellowProfileButton = findViewById<ImageView>(R.id.yellowProfileButton)
        val notsOff = findViewById<AppCompatButton>(R.id.notificationsOff)
        val homeScreenButton = findViewById<Button>(R.id.homeScreenButton)
        val whatsappButton = findViewById<AppCompatButton>(R.id.whatsappButton)
        val refreshPage = findViewById<AppCompatButton>(R.id.profileButton)
        val nameSurnameText = findViewById<TextView>(R.id.nameSurname)
        val cellphoneNumberText = findViewById<TextView>(R.id.cellphoneNumber)
        val emailAddressText = findViewById<TextView>(R.id.emailAddress)
        val birthdayText = findViewById<TextView>(R.id.birthdate)
        val genderText = findViewById<TextView>(R.id.gender)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val currentUserPhoneNumber = currentUser?.phoneNumber

        retrieveUsers()

        if(currentUser==null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        val userList: ArrayList<UserModel> = ArrayList()

        try {
            val obj = JSONObject(readJSONFromAsset()!!)

            val usersArray = obj.getJSONArray("data")

            for (i in 0 until usersArray.length())
            {
                val user = usersArray.getJSONObject(i)

                val id = user.getInt("id")
                val name = user.getString("name")
                val surname = user.getString("surname")
                val email = user.getString("email")
                val birthday = user.getString("birthday")
                val cellphone = user.getString("cellphone")
                val gender = user.getInt("gender")
                val notificationEnabled = user.getBoolean("notificationEnabled")

                val userDetails = UserModel(id,name,surname,email,birthday,cellphone,gender,notificationEnabled)

                userList.add(userDetails)
                if(currentUserPhoneNumber.toString()==cellphone)
                {
                    nameSurnameText.text="$name " + "$surname"
                    cellphoneNumberText.text = cellphone
                    emailAddressText.text = email
                    val birthdayInfo = SpannableStringBuilder()
                        .bold { append(birthdate) }
                        .append(birthday)
                    birthdayText.text=birthdayInfo
                    if(gender==0)
                    {
                        val genderInfoUp = SpannableStringBuilder()
                            .bold { append(sex) }
                            .append("Erkek")
                        genderText.text = genderInfoUp
                    }
                    else if(gender==1)
                    {
                        val genderInfoUp = SpannableStringBuilder()
                            .bold { append(sex) }
                            .append("Kadın")
                        genderText.text = genderInfoUp
                    }

                }
            }

        }
        catch (e: JSONException)
        {
            e.printStackTrace()

        }


        yellowProfileButton.setOnClickListener{

            checkForPermission(Manifest.permission.CAMERA,cameraPerm)
            createNotification()
            val notificationLayout = RemoteViews(packageName,R.layout.custom_notification)
            val builder = NotificationCompat.Builder(this,Channel_ID)
                .setContentTitle("Your Title")
                .setSmallIcon(R.mipmap.appicon)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(this)){
                notify(0,builder.build())
            }

        }

        notsOff.setOnClickListener{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkForPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY,notificationPerm)
            }

        }


        homeScreenButton.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        whatsappButton.setOnClickListener{
            sendMessage(message = "hello")
        }

        refreshPage.setOnClickListener{
            finish()
            startActivity(intent)
        }

    }

    private fun readJSONFromAsset(): String? {
        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val myJsonFile = assets.open("local_data.json")
            val size = myJsonFile.available()
            val buffer = ByteArray(size)
            myJsonFile.read(buffer)
            myJsonFile.close()
            json= String(buffer,charset)

        }
        catch (e: IOException)
        {
            e.printStackTrace()
            return null
        }

        return json
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
    private fun checkForPermission(permission: String, requestCode: Int)
    {
        if(ContextCompat.checkSelfPermission(this@ProfileActivity,permission) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this@ProfileActivity, arrayOf(permission),requestCode)

        }
        else
        {
            //Toast.makeText(this@ProfileActivity,"Permission Granted Already",Toast.LENGTH_LONG).show()
            updateEnableNotifications("7",true)
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == notificationPerm)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this@ProfileActivity, "Notification Permission Granted",Toast.LENGTH_SHORT).show()
                updateEnableNotifications("7",true)

            }
            else
            {
                Toast.makeText(this@ProfileActivity, "Notification Permission Denied",Toast.LENGTH_SHORT).show()
                updateUnenableNotifications("7",false)

            }
        }
        else if(requestCode == cameraPerm)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this@ProfileActivity, "Camera Permission Granted",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this@ProfileActivity, "Camera Permission Denied",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNotification()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "Kahvaltı"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Channel_ID,name,importance).apply {

            }
            val notManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notManager.createNotificationChannel(channel)

        }
    }

    private fun retrieveUsers()  {
        val notsOff = findViewById<AppCompatButton>(R.id.notificationsOff)
        val currentUser = firebaseAuth.currentUser
        val currentUserPhoneNumber = currentUser?.phoneNumber

        db.collection("userdata")
            .get()
            .addOnSuccessListener { result ->
                for(document in result)
                {
                    if(document.data["5"]==currentUserPhoneNumber)
                    {
                        if(document.data["7"]==false)
                        {
                            notsOff.setBackgroundResource(R.mipmap.toggleoff)
                        }
                        else
                        {
                            notsOff.setBackgroundResource(R.mipmap.toggleon)
                        }
                    }
                }

            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }



    private fun updateEnableNotifications(line:String, cond: Boolean){
        val notsOff = findViewById<AppCompatButton>(R.id.notificationsOff)
        val notificationEnabling = hashMapOf(line to cond)
        val currentUser = firebaseAuth.currentUser
        val currentUserPhoneNumber = currentUser?.phoneNumber
        db.collection("userdata")
            .get()
            .addOnSuccessListener { result ->
                for(document in result)
                {
                    if(document.data["5"]==currentUserPhoneNumber) {
                        db.collection("userdata").document(document.id)
                            .set(notificationEnabling, SetOptions.merge())
                        notsOff.setBackgroundResource(R.mipmap.toggleon)
                    }

                }

            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }

    }


    private fun updateUnenableNotifications(line:String, cond: Boolean)
    {
        val notsOff = findViewById<AppCompatButton>(R.id.notificationsOff)
        val notificationEnabling = hashMapOf(line to cond)
        val currentUser = firebaseAuth.currentUser
        val currentUserPhoneNumber = currentUser?.phoneNumber
        db.collection("userdata")
            .get()
            .addOnSuccessListener { result ->
                for(document in result)
                {
                    if(document.data["5"]==currentUserPhoneNumber) {
                        db.collection("userdata").document(document.id)
                            .set(notificationEnabling, SetOptions.merge())
                        notsOff.setBackgroundResource(R.mipmap.toggleoff)
                    }

                }

            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }

    }



}