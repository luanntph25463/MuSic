package com.example.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.Model.Song
import com.example.music.ViewModel.SongsViewModel
import com.example.music.`interface`.OnSongClickListener

class HomeFragment : Fragment(), SongsAdapter.ItemClickListener, SongsAdapter_2.ItemClickListener {
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var songsViewModel: SongsViewModel
    private lateinit var adapter1: SongsAdapter
    private lateinit var adapter2: SongsAdapter_2
    private lateinit var adapter3: SongsAdapter_2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView1 = rootView.findViewById(R.id.recyclerView)
        recyclerView1.layoutManager = GridLayoutManager(context, 2)
        adapter1 = SongsAdapter(emptyList(), this)
        recyclerView1.adapter = adapter1

        recyclerView2 = rootView.findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter2 = SongsAdapter_2(emptyList(), this)
        recyclerView2.adapter = adapter2

        recyclerView3 = rootView.findViewById(R.id.recyclerView3)
        recyclerView3.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter3 = SongsAdapter_2(emptyList(), this)
        recyclerView3.adapter = adapter3

        // Initialize the ViewModel
        songsViewModel = ViewModelProvider(this).get(SongsViewModel::class.java)

        // Observe the songs LiveData
        songsViewModel.getSongsLiveData().observe(viewLifecycleOwner, Observer { songs ->
            // Update the adapters with the list of songs
            adapter1.setSongs(songs)
            adapter2.setSongs(songs)
            adapter3.setSongs(songs)
        })

        // Fetch songs data
        songsViewModel.fetchSongs()

        return rootView
    }

    override fun onItemClick(song: Song) {
        (requireActivity() as? OnSongClickListener)?.onSongClicked(song)
    }
}