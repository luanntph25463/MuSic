package com.example.music.Fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.Model.Song

import com.example.music.R
import com.example.music.SongsAdapter
import com.example.music.SongsAdapter_2
import com.example.music.ViewModel.SongsViewModel


class SearchFragment : Fragment(), SongsAdapter.ItemClickListener, SongsAdapter_2.ItemClickListener {
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var songsViewModel: SongsViewModel
    private lateinit var adapter1: SongsAdapter
    private lateinit var adapter2: SongsAdapter_2
    private lateinit var searchButton: TextView
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        searchButton = rootView.findViewById(R.id.searchButton)
        searchEditText = rootView.findViewById(R.id.searchEditText)

        recyclerView1 = rootView.findViewById(R.id.recyclerView1)
        recyclerView1.layoutManager = GridLayoutManager(context, 2)
        adapter1 = SongsAdapter(emptyList(), this)
        recyclerView1.adapter = adapter1

        recyclerView2 = rootView.findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter2 = SongsAdapter_2(emptyList(), this)
        recyclerView2.adapter = adapter2


        // Initialize the ViewModel
        songsViewModel = ViewModelProvider(requireActivity())[SongsViewModel::class.java]

        // Observethe songs LiveData
        songsViewModel.getSongsLiveData().observe(viewLifecycleOwner) { songs ->
            // Update the adapters with the list of songs
            adapter1.setSongs(songs)
            adapter2.setSongs(songs)
        }
        searchButton.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            songsViewModel.searchSongsByName(searchTerm)
            Log.d(TAG, searchTerm)
            adapter1.setSongs(songsViewModel.getSearchResultSongs())
        }

        // Fetch songs data
        songsViewModel.fetchSongs()

        return rootView
    }

    override fun onItemClick(song: Song) {

    }




}