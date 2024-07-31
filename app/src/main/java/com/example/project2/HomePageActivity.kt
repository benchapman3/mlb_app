package com.example.project2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HomePageActivity : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var searchplayerBtn: Button
    private lateinit var searchteamBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        title=findViewById(R.id.textView)
        searchplayerBtn=findViewById(R.id.searchplayerBtn)
        searchteamBtn=findViewById(R.id.searchteamBtn)

        searchplayerBtn.setOnClickListener {
            val countryIntentActivity = Intent(this
                , PlayerSourceActivity::class.java)
            startActivity(countryIntentActivity)
        }

        searchteamBtn.setOnClickListener {
            val countryIntentActivity = Intent(this
                , RecentSearchesActivity::class.java)
            startActivity(countryIntentActivity)
        }
    }
}