package com.example.kaju

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.StatsLog.logEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.FirebaseException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class EntranceActivity : AppCompatActivity() {


    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var firebaseAnalytics: FirebaseAnalytics




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)

        auth=FirebaseAuth.getInstance()
        firebaseAnalytics = Firebase.analytics

        val login=findViewById<AppCompatButton>(R.id.login_button)

        val currentUser = auth.currentUser

        if(currentUser != null) {
            startActivity(Intent(applicationContext, MainMenu::class.java))
            finish()
        }

        login.setOnClickListener{
            login()
        }


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainMenu::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token

                val intent = Intent(applicationContext,VerificationActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }
        }




    }

    private fun login() {
        val mobileNumber=findViewById<EditText>(R.id.phoneNumber)
        val number=mobileNumber.text.toString().trim()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_NAME,number)
        }

        if(number.isNotEmpty()){
            sendVerificationCode(number)
        }else{
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

}