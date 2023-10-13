package com.example.music.Repository

import com.example.music.Model.Song
import com.example.music.R
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject


import kotlinx.coroutines.withContext


class SongsRepository {
    suspend fun fetchSongs(): List<Song> = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://shazam.p.rapidapi.com/songs/list-recommendations?key=484129036&locale=en-US")
                .get()
                .addHeader("X-RapidAPI-Key", "40a4273997msh9f1613a2f0a8952p136483jsn249cb4458e00")
                .addHeader("X-RapidAPI-Host", "shazam.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                return@withContext parseSongs(responseBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext emptyList()
    }

    private fun parseSongs(responseBody: String?): List<Song> {
        val songs = mutableListOf<Song>()
        // initialization list  Songs  get value in JSON  add value to Songs
        try {
            val jsonObject = JSONObject(responseBody!!)
            val jsonArray = jsonObject.getJSONArray("tracks")

            for (i in 0 until jsonArray.length()) {
                val trackObject = jsonArray.getJSONObject(i)
                val title = trackObject.getString("title")
                val imageUrl = trackObject.getJSONObject("share").getString("image")
                // not use do exoplayer not play
//                val url = trackObject.getJSONObject("share").getString("href")
                val key = trackObject.getString("key")

                val song = Song(
                    Song.getNextId(),
                    key,
                    title,
                    imageUrl,
                    "android.resource://com.example.music/${R.raw.catdoinoisau}"
                )
                songs.add(song)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return songs
    }
}