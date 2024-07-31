package com.example.project2

import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerSourceActivity : AppCompatActivity() {

    private lateinit var searchlastname: EditText
    private lateinit var searchBtn: Button
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_source)

        searchlastname=findViewById(R.id.searchlastname)
        searchBtn=findViewById(R.id.searchBtn)
        recycler=findViewById(R.id.RecyclerView)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        searchBtn.setOnClickListener {
            getPlayerInfo(searchlastname.text.toString())
        }

    }

    private fun getPlayerInfo(lname: String) {
        val Manager=Manager()

        var players=listOf<PlayerInfo>()
        CoroutineScope(Dispatchers.IO).launch {
            val Key = getString(R.string.Key)
            players=Manager.retrievePlayers(lname, Key)
            if (players.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    val adapter = Adapter(players)
                    recycler.adapter = adapter
                    recycler.layoutManager = LinearLayoutManager(
                        this@PlayerSourceActivity
                    )
                }
            }
        }
    }
}