package com.example.music.Fragment

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.music.R
import com.example.music.ViewModel.SongsViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

@Suppress("DEPRECATION", "NAME_SHADOWING")
class DetailFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var btnPause: ImageButton
    private lateinit var previous: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var songsViewModel: SongsViewModel
    private lateinit var seekBar: SeekBar
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var replace: ImageButton
    private lateinit var btnForward: ImageButton
    private var replaceButtonClicked = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        handler = Handler()
        runnable = Runnable { updateSeekBar() }
        imageView = view.findViewById(R.id.imageView2)
        titleTextView = view.findViewById(R.id.tenBai)
        btnPause = view.findViewById(R.id.btnPause)
        seekBar = view.findViewById(R.id.seekBar)
        replace = view.findViewById(R.id.replace)
        btnForward = view.findViewById(R.id.btnForward)
        previous = view.findViewById(R.id.btnRewind)
        btnNext = view.findViewById(R.id.flush)

        songsViewModel = ViewModelProvider(requireActivity())[SongsViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //
        songsViewModel.run {
            getSelectedSong().observe(viewLifecycleOwner) { song ->
                // Update the UI with the selected song
                titleTextView.text = song.title
                Glide.with(requireContext()).load(song.image).into(imageView)
            }
        }
        // Initialize ExoPlayer
        val currentSong = songsViewModel.getSelectedSong().value
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

        // set  MediaItem từ URL  của bài hát
        val mediaItem = currentSong?.let { MediaItem.fromUri(it.href) }
        Log.d(TAG,"$mediaItem")
        // check media not empty  get MediaItem cho ExoPlayer
        if (mediaItem != null) {
            exoPlayer.setMediaItem(mediaItem)
        }

        // prepare và play
        exoPlayer.prepare()
        exoPlayer.play()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // get vị trí mới in tỉ lệ
                    val newPosition = (exoPlayer.duration * progress) / seekBar!!.max
                    exoPlayer.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Stop updating the playback position when the user starts dragging and dropping
                exoPlayer.playWhenReady = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Disable updating playback position when user finishes dragging and dropping
                exoPlayer.playWhenReady = true
            }
        })
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    ExoPlayer.STATE_ENDED, ExoPlayer.STATE_IDLE -> {
                        handler.removeCallbacks(runnable)
                        seekBar.progress = 0
                    }
                    else -> updateSeekBar()
                }
            }
        })

        // // previous song
        previous.setOnClickListener {
                // get value song current
                val currentSong = songsViewModel.getSelectedSong().value
                Log.d("currentSong", "$currentSong")

                // check song current
                val previousSong = currentSong?.let { it1 -> songsViewModel.getPreviousSong(it1) }
                // if not null Toast
                // if return song set title and url
                if (previousSong != null) {
                    titleTextView.text = previousSong.title

                    // Use Glide to load and display the image from the previous song (e.g., previousSong.image)
                    Glide.with(this)
                        .load(previousSong.image)
                        .into(imageView)
                        // play song
                    songsViewModel.setSelectedSong(previousSong)
                    val mediaItem = MediaItem.fromUri(previousSong.href)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
                }else{
                    Toast.makeText(context,"Hết Bài",Toast.LENGTH_SHORT).show()
                }
        }
        // next Songs
        btnNext.setOnClickListener {
                // get selcted Song Current
                val currentSong = songsViewModel.getSelectedSong().value
                Log.d("currentSong", "$currentSong")

                val nextSong = currentSong?.let { it1 -> songsViewModel.getNextSong(it1) }

                if (nextSong != null) {
                    titleTextView.text = nextSong.title

                    // Use Glide to load and display the image from the previous song (e.g., previousSong.image)
                    Glide.with(this)
                        .load(nextSong.image)
                        .into(imageView)

                    songsViewModel.setSelectedSong(nextSong)
                    val mediaItem = MediaItem.fromUri(nextSong.href)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
                }else{
                    Toast.makeText(context,"Hết Bài",Toast.LENGTH_SHORT).show()
                }
            }

        btnPause.setOnClickListener {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
                Log.d("Đã Pause", "s")
                btnPause.setImageResource(R.drawable.pause)
            } else {
                exoPlayer.play()
                Log.d("Đã play", "s")
                btnPause.setImageResource(R.drawable.play)
            }
        }

        replace.setOnClickListener {
            if (!replaceButtonClicked) {
                replaceButtonClicked = true
                //listen to events
                exoPlayer.addListener(object : Player.EventListener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            //  check  all state in exoplayer if  exoplayer end  or not phat lai
                            ExoPlayer.STATE_ENDED, ExoPlayer.STATE_IDLE -> {
                                handler.removeCallbacks(runnable)
                                // if  replaceButtonClicked = true start play
                                if (replaceButtonClicked) {
                                    exoPlayer.seekTo(0)
                                    exoPlayer.play()
                                }
                            }
                            else -> updateSeekBar()
                        }
                    }
                })
            } else {
                replaceButtonClicked = false
                exoPlayer.stop()
            }
        }

            // share song current
        btnForward.setOnClickListener {
            val currentSong = songsViewModel.getSelectedSong().value
            val songTitle = currentSong?.title
            val songUri = currentSong?.href

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Song")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this song: $songTitle\n\n$songUri")

            val shareChooserIntent = Intent.createChooser(shareIntent, "Share Song")
            startActivity(shareChooserIntent)
        }
    }
    // fun update seekbar
    private fun updateSeekBar() {
        // get seekbar.max = seekbar current
        seekBar.max = exoPlayer.duration.toInt()
        seekBar.progress = exoPlayer.currentPosition.toInt()
        handler.postDelayed(runnable, 1000) // Update every second
    }
}