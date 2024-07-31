package com.example.project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerSeasonsActivity : AppCompatActivity() {

    private lateinit var nameText: TextView
    private lateinit var recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_seasons)
        val playerId = intent.getStringExtra("playerId")
        val playerfname = intent.getStringExtra("playerfname")
        val playerlname = intent.getStringExtra("playerlname")

        nameText=findViewById(R.id.nameText)
        recycler=findViewById(R.id.RecyclerSeasons)
        nameText.text = "$playerfname $playerlname"

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        var seasons=listOf<Seasons>()
        CoroutineScope(Dispatchers.IO).launch {
            val Key = getString(R.string.Key)
            seasons=Manager().getPlayerSeasons(playerId.toString(),Key)
            withContext(Dispatchers.Main) {
                val adapter = SeasonsAdapter(seasons)
                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(
                    this@PlayerSeasonsActivity)
            }

        }

    }
}