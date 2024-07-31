package com.example.project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecentSearchesActivity : AppCompatActivity() {
    // Initialize Firebase reference
    private val database = FirebaseDatabase.getInstance()
    private val recentSearchesRef = database.getReference("recent_searches")
    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_searches)
        textView=findViewById(R.id.textView2)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Set up RecyclerView to display recent searches
        recyclerView=findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve recent searches from Firebase Realtime Database
        recentSearchesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val recentSearchesList = mutableListOf<PlayerInfo>()
                for (snapshot in dataSnapshot.children) {
                    val player = snapshot.getValue(PlayerInfo::class.java)
                    player?.let { recentSearchesList.add(it) }
                }
                // Set up RecyclerView adapter with recent searches list
                val adapter = Adapter(recentSearchesList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Log.e("RecentSearchesActivity", "Database error: ${databaseError.message}")
            }
        })
    }
}