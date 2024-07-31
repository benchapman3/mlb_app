package com.example.project2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class Adapter(val players: List<PlayerInfo>): RecyclerView.Adapter<Adapter.ViewHolder>(){

    inner class ViewHolder(rootLayout: View): RecyclerView.ViewHolder(rootLayout), View.OnClickListener {
        val lname: TextView =rootLayout.findViewById(R.id.lname)
        val fname: TextView =rootLayout.findViewById(R.id.fname)
        val team: TextView =rootLayout.findViewById(R.id.team)
        val position: TextView =rootLayout.findViewById(R.id.position)

        init {
            rootLayout.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val player = players[adapterPosition]

            val database = FirebaseDatabase.getInstance()
            val recentSearchRef = database.getReference("recent_searches").child(player.id)
            recentSearchRef.setValue(player)

            val intent = Intent(v?.context, PlayerSeasonsActivity::class.java)
            intent.putExtra("playerId", player.id)
            intent.putExtra("playerfname", player.fname)
            intent.putExtra("playerlname", player.lname)
            v?.context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("VH", "inside onCreateViewHolder")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View=layoutInflater.inflate(R.layout.player_card_view, parent, false)
        val viewHolder=ViewHolder(rootLayout)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position1: Int) {
        val currentPlayer=players[position1]
        holder.lname.text= currentPlayer.lname
        holder.fname.text= currentPlayer.fname
        holder.team.text=currentPlayer.team
        holder.position.text=currentPlayer.position
        Log.d("VH", "inside onBindViewHolder on position $position1")
    }
}

