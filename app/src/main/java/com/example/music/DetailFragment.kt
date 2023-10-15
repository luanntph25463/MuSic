package com.example.music

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.music.Model.Song
import com.example.music.ViewModel.SongsViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer


class DetailFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var btnPause: ImageButton
    private lateinit var exoPlayer: SimpleExoPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        imageView = view.findViewById(R.id.imageView2)
        titleTextView = view.findViewById(R.id.tenBai)
        btnPause = view.findViewById(R.id.btnPause)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ExoPlayer
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

        // Get selectedSong object from ViewModel
        val viewModel = ViewModelProvider(requireActivity()).get(SongsViewModel::class.java)
        val selectedSong = viewModel.getSelectedSong()
        Log.d("s","$selectedSong")
        // Set title to TextView
        if (selectedSong != null) {
            titleTextView.text = selectedSong.title
        }

        // Use Glide to load and display image from URL or resource
        if (selectedSong != null) {
            Glide.with(this)
                .load(selectedSong.image)
                .into(imageView)
        }

        // Set ExoPlayer for ExoPlayerView

        // Handle click event on btnPause
        btnPause.setOnClickListener {
            val viewModel = ViewModelProvider(requireActivity()).get(SongsViewModel::class.java)
            val selectedSong = viewModel.getSelectedSong()

            if (selectedSong != null) {
                playVideoAudio(selectedSong.href)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Release ExoPlayer when Fragment is destroyed
        exoPlayer.release()
    }

    fun playVideoAudio(videoUrl: String) {
        // Check if ExoPlayer is initialized
        if (!::exoPlayer.isInitialized) {
            return
        }

        // Create MediaItem from video URL
        val mediaItem = MediaItem.fromUri(videoUrl)

        // Set MediaItem for ExoPlayer
        exoPlayer.setMediaItem(mediaItem)

        // Prepare ExoPlayer
        exoPlayer.prepare()

        // Play ExoPlayer
        exoPlayer.play()
    }
}