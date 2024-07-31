package com.example.project2

import android.provider.Settings.Global.getString
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject

class Manager {

    val okHttpClient: OkHttpClient
    init {
        val builder=OkHttpClient.Builder()
        val loggingInterceptor=HttpLoggingInterceptor()
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        okHttpClient=builder.build()

    }

    fun retrievePlayers(lname: String, Key: String): List<PlayerInfo> {

        val request=Request.Builder()
            .url("https://baseballapi.p.rapidapi.com/api/baseball/search/$lname")
            .header("X-RapidAPI-Key", "$Key")
            .get()
            .build()

        val response: Response=okHttpClient.newCall(request).execute()
        val responseBody=response.body?.string()
        Log.d("response", "$responseBody")
        if (response.isSuccessful && !responseBody.isNullOrEmpty()){
            val players= mutableListOf<PlayerInfo>()

            val jsonResponse = JSONObject(responseBody)
            val resultsArray = jsonResponse.getJSONArray("results")


            for (i in 0 until resultsArray.length()) {
                val articleObject = resultsArray.getJSONObject(i).getJSONObject("entity")

                if (articleObject != null && articleObject.has("lastName")) {
                    val lname = articleObject.getString("lastName")
                    val fname = articleObject.getString("firstName")
                    val id = articleObject.getInt("id").toString()
                    val team = articleObject.getJSONObject("team").getString("name")
                    val position = articleObject.getString("position")

                    val playerSource = PlayerInfo(lname, fname, team, position, id)
                    players.add(playerSource)
                }
            }

            return players
        }else {
            return listOf()
        }
    }

    fun retrievePlayerStats(playerID: String, seasonID: String, tournID: String, Key: String): List<PlayerStats> {
        val request = Request.Builder()
            .url("https://baseballapi.p.rapidapi.com/api/baseball/player/$playerID/tournament/$tournID/season/$seasonID/statistics/regularSeason")
            .header("X-RapidAPI-Key", "$Key")
            .get()
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()
        Log.d("response", "$responseBody")
        val stats = mutableListOf<PlayerStats>()
        if (response.isSuccessful && responseBody != null) {
            val jsonObject = JSONObject(responseBody)
            val statisticsObject = jsonObject.getJSONObject("statistics")

            val p_k= statisticsObject.optInt("pitchingStrikeOuts")
            val p_w = statisticsObject.optInt("pitchingBaseOnBalls")
            val p_hits = statisticsObject.optInt("pitchingHits")
            val p_ab = statisticsObject.optInt("pitchingAtBats")
            val p_era = statisticsObject.optInt("pitchingEra")
            val p_wins = statisticsObject.optInt("pitchingWins")
            val p_losses = statisticsObject.optInt("pitchingLosses")
            val p_saves = statisticsObject.optInt("pitchingSaves")
            val p_numpitches = statisticsObject.optInt("pitchingNumberOfPitches")
            val p_hr = statisticsObject.optInt("pitchingHomeRuns")

            val h_games = statisticsObject.optInt("hittingGamesPlayed")
            val h_hits = statisticsObject.optInt("hittingHits")
            val h_k = statisticsObject.optInt("hittingStrikeOuts")
            val h_avg = statisticsObject.optDouble("hittingAvg")
            val h_ab = statisticsObject.optInt("hittingAtBats")
            val h_w = statisticsObject.optInt("hittingBaseOnBalls")
            val h_hr = statisticsObject.optInt("hittingHomeRuns")

            val statsSource = PlayerStats(h_games.toString(), h_ab.toString(), h_avg.toString() ,
                h_hits.toString(), h_w.toString(), h_k.toString(), h_hr.toString(), p_k.toString(),
                p_w.toString(), p_hits.toString(), p_ab.toString(), p_era.toString(), p_wins.toString(),
                p_losses.toString(), p_saves.toString(), p_numpitches.toString(), p_hr.toString())
            stats.add(statsSource)

            return stats
        } else {
            Log.e("Stats", "Error: ${response.code}")
            return listOf()
        }
    }

    fun getPlayerSeasons(id: String, Key: String): List<Seasons> {
        val request = Request.Builder()
            .url("https://baseballapi.p.rapidapi.com/api/baseball/player/$id/statistics/seasons")
            .header("X-RapidAPI-Key", "$Key")
            .get()
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()
        Log.d("response", "$responseBody")
        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val seasons = mutableListOf<Seasons>()

            try {
                val jsonResponse = JSONObject(responseBody)
                val uniqueTournamentSeasons = jsonResponse.getJSONArray("uniqueTournamentSeasons")

                val tournamentSeason = uniqueTournamentSeasons.getJSONObject(0)
                val uniqueTournament = tournamentSeason.getJSONObject("uniqueTournament")
                val tournamentId = uniqueTournament.getInt("id")

                for (i in 0 until uniqueTournamentSeasons.length()) {
                    val tournamentSeason = uniqueTournamentSeasons.getJSONObject(i)
                    val seasonsArray = tournamentSeason.getJSONArray("seasons")

                    for (j in 0 until seasonsArray.length()) {
                        val season = seasonsArray.getJSONObject(j)
                        val year = season.getInt("year")
                        val seasonID = season.getInt("id")
                        Log.d("Season ID", seasonID.toString())
                        val seasonSource = Seasons(year.toString(), seasonID.toString(), id, tournamentId.toString())
                        Log.d("seasons added for playerid $id", "$season + $seasonID + $tournamentId")
                        seasons.add(seasonSource)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return seasons
        } else {
            return listOf()
        }
    }
}