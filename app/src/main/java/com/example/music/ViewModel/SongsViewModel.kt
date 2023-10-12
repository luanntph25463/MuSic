package com.example.music.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.music.Adapter.SongsDataHolder
import com.example.music.Adapter.SongsDataHolder.isDataAdded
import com.example.music.Model.Song
import com.example.music.R
import com.example.music.`interface`.OnSongClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

class SongsViewModel : ViewModel() {
    private val songsLiveData = MutableLiveData<List<Song>>()
    private var songClickListener: OnSongClickListener? = null
    private val selectedSongLiveData = MutableLiveData<Song>()


    fun setSongClickListener(listener: OnSongClickListener) {
        songClickListener = listener
    }

    fun fetchSongs() {
        GlobalScope.launch(Dispatchers.IO) {
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
                    val songs = parseSongs(responseBody)
                        Log.d("isDataAdded", "$isDataAdded")

                        songsLiveData.postValue(songs)
                        Log.d("âsasa", "Fetching songs")
                        // Save fetchCount value to SharedPreferences


                    Log.d("all","$songs")
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
                val url = trackObject.getJSONObject("share").getString("href")
                val key = trackObject.getString("key")
                Log.d("TAG", "$key")


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

    fun getPreviousSong(currentSong: Song, songs: List<Song>?): Song? {
        val songs = getSongsLiveData().value
        Log.d("getPreviousSong", "$songs")
        if (songs != null) {
            val currentId = currentSong.id
            Log.d("currentId", "$currentId")
            val currentPosition = songs.indexOfFirst { it.id == currentId }
            Log.d("currentPosition", "$currentPosition")
            if (currentPosition > 0) {
                return songs[currentPosition - 1]
            }else{
                return null
            }
        }
        return null
    }
    fun getNextsSong(currentSong: Song, songs: List<Song>?): Song? {
        val songs = getSongsLiveData().value
        Log.d("getPreviousSong", "$songs")
        if (songs != null) {
            val currentId = currentSong.id
            Log.d("currentId", "$currentId")
            val currentPosition = songs.indexOfFirst { it.id == currentId }
            Log.d("currentPosition", "$currentPosition")
            if (currentPosition < songs.size - 1) {
                return songs[currentPosition + 1]
            }else{
                return null
            }
        }
        return null
    }

    fun setSelectedSong(song: Song) {
        selectedSongLiveData.value = song
    }
    fun getSongsLiveData(): LiveData<List<Song>> {
        return songsLiveData
    }

    fun getSelectedSong(): LiveData<Song> {
        return selectedSongLiveData
    }


}