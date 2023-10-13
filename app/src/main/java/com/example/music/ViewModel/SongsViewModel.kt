package com.example.music.ViewModel


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

    fun fetchSongs() {
        viewModelScope.launch {
            try {
                val songs = songsRepository.fetchSongs()
//                get value  in API  postValues in Live_Data
                songsLiveData.postValue(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    // get value  previous song current
    fun getPreviousSong(currentSong: Song): Song? {
        // get list list song in liveData
        val songsList = songsLiveData.value
        // if not empty
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
    // set song
    fun setSelectedSong(song: Song) {
        selectedSongLiveData.value = song
    }
    // get list
    fun getSongsLiveData(): LiveData<List<Song>> {
        return songsLiveData
    }
    // get song
    fun getSelectedSong(): LiveData<Song> {
        return selectedSongLiveData
    }
}