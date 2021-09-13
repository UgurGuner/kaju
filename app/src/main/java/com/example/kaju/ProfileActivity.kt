package com.example.kaju

import android.Manifest
import android.R.attr
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Person
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.nio.charset.Charset
import android.R.attr.data
import android.app.ProgressDialog
import android.content.SharedPreferences

import android.net.Uri
import android.util.Base64
import android.graphics.BitmapFactory
import android.view.ViewGroup
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore
    private val notificationPerm = 101
    private val cameraPerm = 102
    private val storagePermRead = 103
    private val storagePermWrite = 104
    private val Channel_ID = "Your_Channel_ID"
    private val birthdate = "Doğum Tarihi: "
    private val sex = "Cinsiyet: "
    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val textDailyPlan = findViewById<TextView>(R.id.textDailyPlan)
        val plusButton = findViewById<AppCompatButton>(R.id.plusButton)
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

        val f = File(
            "/data/data/com.example.kaju/shared_prefs/sharedPreferences.xml"
        )
       if(f.exists())
       {
           val prefs = getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE)
           val img = prefs.getString("STRING_KEY","")
           val imageBytes = Base64.decode(img,Base64.DEFAULT)
           val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
           profileIcon.setImageBitmap(decodedImage)
           yellowProfileButton.setImageBitmap(decodedImage)
       }

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

            checkForPermissionCamera(Manifest.permission.CAMERA,cameraPerm)
            checkForPermissionStorage(Manifest.permission.READ_EXTERNAL_STORAGE,storagePermRead)
            checkForPermissionStorage(Manifest.permission.WRITE_EXTERNAL_STORAGE,storagePermWrite)
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

            startActivity(Intent(this,MainMenu::class.java))
            finish()
        }

        val signOut = findViewById<AppCompatButton>(R.id.signOut)
        signOut.setOnClickListener{
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

        plusButton.setOnClickListener{
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

    private fun checkForPermissionStorage(permission: String, requestCode: Int)
    {
        if(ContextCompat.checkSelfPermission(this@ProfileActivity,permission) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this@ProfileActivity, arrayOf(permission),requestCode)

        }
        else
        {
            //Toast.makeText(this@ProfileActivity,"Permission Granted Already",Toast.LENGTH_LONG).show()

        }


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
    private fun checkForPermissionCamera(permission: String, requestCode: Int)
    {
        if(ContextCompat.checkSelfPermission(this@ProfileActivity,permission) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this@ProfileActivity, arrayOf(permission),requestCode)

        }
        else
        {
            dispatchTakePictureIntent()
            //Toast.makeText(this@ProfileActivity,"Permission Granted Already",Toast.LENGTH_LONG).show()
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


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val yellowProfileButton = findViewById<ImageView>(R.id.yellowProfileButton)
        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {


            val currentUser = firebaseAuth.currentUser
            val currentUserPhoneNumber = currentUser?.phoneNumber
            val image = data?.extras?.get("data") as Bitmap
            yellowProfileButton.setImageBitmap(image)
            try {
                val byteArrayOutputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
                val bytes: ByteArray = byteArrayOutputStream.toByteArray()
                val sImage = Base64.encodeToString(bytes,Base64.DEFAULT)
                val sharedPreferences = getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.apply{
                    putString("STRING_KEY",sImage)
                }.apply()

                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Uploading file...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val storageRef = FirebaseStorage.getInstance().getReference("/data/data/com.example.kaju/shared_prefs/The profile picure of the user$currentUserPhoneNumber")
                storageRef.putBytes(bytes)
                progressDialog.dismiss()



            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }


        }
    }

}
