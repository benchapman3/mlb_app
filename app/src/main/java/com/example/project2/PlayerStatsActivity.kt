package com.example.project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerStatsActivity : AppCompatActivity() {

    private lateinit var tableLayoutHitting: TableLayout
    private lateinit var tableLayoutPitching: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_stats)

        tableLayoutHitting = findViewById(R.id.tableLayoutHitting)
        tableLayoutPitching = findViewById(R.id.tableLayoutPitching)

        val year = intent.getStringExtra("year")
        val playerID = intent.getStringExtra("playerID")
        val seasonID = intent.getStringExtra("seasonID")
        val tournamentID = intent.getStringExtra("tournID")
        val key = getString(R.string.Key)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.yearTextView).text = year

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val manager = Manager()
                val stats = manager.retrievePlayerStats(playerID.toString(), seasonID.toString(), tournamentID.toString(), key)

                withContext(Dispatchers.Main) {
                    populateTable(tableLayoutHitting, stats.get(0))
                    populatePitchingTable(tableLayoutPitching, stats.get(0))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun populateTable(tableLayout: TableLayout, playerStats: PlayerStats) {
        playerStats.let {
            addRowToTable(tableLayout, "Games", it.h_games)
            addRowToTable(tableLayout, "At Bats", it.h_ab)
            addRowToTable(tableLayout, "Average", it.h_avg)
            addRowToTable(tableLayout, "Hits", it.h_hits)
            addRowToTable(tableLayout, "Wins", it.h_w)
            addRowToTable(tableLayout, "Strikeouts", it.h_k)
            addRowToTable(tableLayout, "Home Runs", it.h_hr)
        }
    }

    private fun populatePitchingTable(tableLayout: TableLayout, playerStats: PlayerStats?) {
        playerStats?.let {
            addRowToTable(tableLayout, "Strikouts", it.p_k)
            addRowToTable(tableLayout, "Walks", it.p_w)
            addRowToTable(tableLayout, "At Bats", it.p_ab)
            addRowToTable(tableLayout, "ERA", it.p_era)
            addRowToTable(tableLayout, "Wins", it.p_wins)
            addRowToTable(tableLayout, "Losses", it.p_losses)
            addRowToTable(tableLayout, "Hits", it.p_hits)
            addRowToTable(tableLayout, "Pitches", it.p_numpitches)
            addRowToTable(tableLayout, "Saves", it.p_saves)
            addRowToTable(tableLayout, "Homeruns", it.p_hr)
        }
    }

    private fun addRowToTable(tableLayout: TableLayout, label: String, value: String) {
        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        row.layoutParams = layoutParams

        val textViewLabel = TextView(this)
        textViewLabel.text = label
        textViewLabel.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)

        val textViewValue = TextView(this)
        textViewValue.text = value
        textViewValue.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)

        row.addView(textViewLabel)
        row.addView(textViewValue)

        tableLayout.addView(row)
    }
}