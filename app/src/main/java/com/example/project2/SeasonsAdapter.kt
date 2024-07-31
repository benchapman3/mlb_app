package com.example.project2

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SeasonsAdapter(val seasons: List<Seasons>): RecyclerView.Adapter<SeasonsAdapter.ViewHolder>(){

    inner class ViewHolder(rootLayout: View): RecyclerView.ViewHolder(rootLayout), View.OnClickListener {
        val year: TextView = rootLayout.findViewById(R.id.year)

        init {
            rootLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val season = seasons[adapterPosition]
            val intent = Intent(v?.context, PlayerStatsActivity::class.java)
            intent.putExtra("year", season.year)
            intent.putExtra("seasonID", season.seasonID)
            intent.putExtra("playerID", season.playerID)
            intent.putExtra("tournID", season.tournamentID)
            v?.context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("VH", "inside onCreateViewHolder")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.seasons_card_view, parent, false)
        return ViewHolder(rootLayout)
    }

    override fun getItemCount(): Int {
        return seasons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSeason = seasons[position]
        holder.year.text = currentSeason.year
        Log.d("VH", "inside onBindViewHolder on position $position")
    }
}

