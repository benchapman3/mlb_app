package com.example.project2

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences=getSharedPreferences("project2", Context.MODE_PRIVATE)
        username=findViewById(R.id.username)
        password=findViewById(R.id.password)
        loginBtn=findViewById(R.id.loginBtn)
        signupBtn=findViewById(R.id.signupBtn)
        firebaseAuth= FirebaseAuth.getInstance()

        val savedUsername=preferences.getString("SAVED_USERNAME","")
        val savedPassword=preferences.getString("SAVED_PASSWORD","")
        username.setText(savedUsername)
        password.setText(savedPassword)

        signupBtn.setOnClickListener {
            val inputtedUserName:String=username.text.toString().trim()
            val inputtedPassword:String=password.text.toString().trim()
            firebaseAuth.createUserWithEmailAndPassword(inputtedUserName,inputtedPassword)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        val user=firebaseAuth.currentUser
                        Toast.makeText(this,"Created user: ${user!!.email}", Toast.LENGTH_LONG).show()
                    }else {
                        val exception=it.exception
                        Toast.makeText(this, "Failed: $exception", Toast.LENGTH_LONG).show()
                    }
                }
        }

        loginBtn.setOnClickListener {

            val inputtedUserName:String=username.text.toString().trim()
            val inputtedPassword:String=password.text.toString().trim()
            firebaseAuth.signInWithEmailAndPassword(inputtedUserName, inputtedPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user=firebaseAuth.currentUser
                        Toast.makeText(this, "Logged in as: ${user!!.email}", Toast.LENGTH_LONG).show()
                        preferences.edit().putString("SAVED_USERNAME",username.text.toString()).apply()
                        preferences.edit().putString("SAVED_PASSWORD",password.text.toString()).apply()
                        val countryIntentActivity = Intent(this@MainActivity, HomePageActivity::class.java)
                        startActivity(countryIntentActivity)
                    }else {
                        val exception=it.exception
                        Toast.makeText(this, "Failed $exception", Toast.LENGTH_LONG).show()
                    }
                }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(this@MainActivity, "Build version is Tiramasu ${Build.VERSION.SDK_INT}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@MainActivity, "Build version is less than Tiramasu ${Build.VERSION.SDK_INT}", Toast.LENGTH_LONG).show()
        }


    }
}