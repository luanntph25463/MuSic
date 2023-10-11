package com.example.music.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music.Model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

class SongsViewModel : ViewModel() {
    private val songsLiveData = MutableLiveData<List<Song>>()

    fun getSongsLiveData(): LiveData<List<Song>> {
        return songsLiveData
    }

    fun fetchSongs() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Perform network request to fetch songs data
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
                    // Parse the response and extract the list of songs
                    val songs = parseSongs(responseBody)
                    songsLiveData.postValue(songs)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseSongs(responseBody: String?): List<Song> {
        val songs = mutableListOf<Song>()

        try {
            val jsonObject = JSONObject(responseBody)
            val jsonArray = jsonObject.getJSONArray("tracks")

            for (i in 0 until jsonArray.length()) {
                val trackObject = jsonArray.getJSONObject(i)
                val title = trackObject.getString("title")
                val imageUrl = trackObject.getJSONObject("share").getString("image")
                val key = trackObject.getString("key")
                Log.d("TAG", "$key")
                val song = Song(key,title,imageUrl)
                songs.add(song)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return songs
    }
}