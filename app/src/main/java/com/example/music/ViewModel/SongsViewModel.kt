package com.example.music.ViewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.Model.Song
import com.example.music.Repository.SongsRepository

import kotlinx.coroutines.launch

class SongsViewModel : ViewModel() {
    private val songsLiveData = MutableLiveData<List<Song>>()
    private val selectedSongLiveData = MutableLiveData<Song>()
    private val songsRepository = SongsRepository()
    private var searchResultSongs = MutableLiveData<List<Song>>()


    fun fetchSongs() {
        viewModelScope.launch {
            try {
                val songs = songsRepository.fetchSongs()
                songsLiveData.postValue(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPreviousSong(currentSong: Song): Song? {
        val songsList = songsLiveData.value
        if (songsList != null) {
            val currentId = currentSong.id
            val currentPosition = songsList.indexOfFirst { it.id == currentId }
            if (currentPosition > 0) {
                return songsList[currentPosition - 1]
            }
        }
        return null
    }

    fun getNextSong(currentSong: Song): Song? {
        val songsList = songsLiveData.value
        if (songsList != null) {
            val currentId = currentSong.id
            val currentPosition = songsList.indexOfFirst { it.id == currentId }
            if (currentPosition < songsList.size - 1) {
                return songsList[currentPosition + 1]
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
    fun searchSongsByName(searchTerm: String) {
        val allSongsList = songsLiveData.value
        Log.d("sss","$allSongsList")
        if (allSongsList != null) {
            val filteredSongs = allSongsList.filter { song ->
                song.title.contains(searchTerm, ignoreCase = true)
            }
            searchResultSongs.value = filteredSongs
        }
    }
    fun getSearchResultSongs(): List<Song> {
        return searchResultSongs.value!!
    }

}