package com.example.music

import android.content.Intent
import android.media.MediaPlayer
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
import com.example.music.ViewModel.SongsViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class DetailFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var btnPause: ImageButton
    private lateinit var previous: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var songsViewModel: SongsViewModel
    private lateinit var mediaPlayer: MediaPlayer
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

        songsViewModel = ViewModelProvider(requireActivity()).get(SongsViewModel::class.java)
        songsViewModel.run {
            getSelectedSong().observe(viewLifecycleOwner) { song ->
                // Update the UI with the selected song
                titleTextView.text = song.title
                Glide.with(requireContext()).load(song.image).into(imageView)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer()

        // Initialize ExoPlayer
        val currentSong = songsViewModel.getSelectedSong().value
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

// Tạo một MediaItem từ URL hoặc đường dẫn của bài hát
        val mediaItem = currentSong?.let { MediaItem.fromUri(it.href) }
        Log.d("mediaItem","$mediaItem")
// Đặt MediaItem cho ExoPlayer
        if (mediaItem != null) {
            exoPlayer.setMediaItem(mediaItem)
            Log.d("ay", "s")
        }
// Chuẩn bị và phát nhạc
        exoPlayer.prepare()
        exoPlayer.play()
        Log.d("Đã play", "s")

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // Tính toán vị trí mới dựa trên tỉ lệ phần trăm
                    val newPosition = (exoPlayer.duration * progress) / seekBar!!.max
                    exoPlayer.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Dừng việc cập nhật vị trí phát khi người dùng bắt đầu kéo thả
                exoPlayer.playWhenReady = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Tắt việc cập nhật vị trí phát khi người dùng kết thúc kéo thả
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


        previous = view.findViewById(R.id.btnRewind)
        btnNext = view.findViewById(R.id.flush)
        // // previous song
        previous.setOnClickListener {
            val songs = songsViewModel.getSongsLiveData().value
            Log.d("songs", "$songs")
            if (songs != null) {
                val currentSong = songsViewModel.getSelectedSong().value
                Log.d("currentSong", "$currentSong")

                val previousSong = currentSong?.let { it1 -> songsViewModel.getPreviousSong(it1, songs) }

                if (previousSong != null) {
                    titleTextView.text = previousSong.title

                    // Use Glide to load and display the image from the previous song (e.g., previousSong.image)
                    Glide.with(this)
                        .load(previousSong.image)
                        .into(imageView)

                    songsViewModel.setSelectedSong(previousSong)
                    val mediaItem = MediaItem.fromUri(previousSong.href)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
                }else{
                    Toast.makeText(context,"Hết Bài",Toast.LENGTH_SHORT).show()
                }
            }
        }
        // next Songs
        btnNext.setOnClickListener {
            val songs = songsViewModel.getSongsLiveData().value
            Log.d("songs", "$songs")
            if (songs != null) {
                val currentSong = songsViewModel.getSelectedSong().value
                Log.d("currentSong", "$currentSong")

                val previousSong = currentSong?.let { it1 -> songsViewModel.getNextsSong(it1, songs) }

                if (previousSong != null) {
                    titleTextView.text = previousSong.title

                    // Use Glide to load and display the image from the previous song (e.g., previousSong.image)
                    Glide.with(this)
                        .load(previousSong.image)
                        .into(imageView)

                    songsViewModel.setSelectedSong(previousSong)
                    val mediaItem = MediaItem.fromUri(previousSong.href)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
                }else{
                    Toast.makeText(context,"Hết Bài",Toast.LENGTH_SHORT).show()
                }
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
                exoPlayer.addListener(object : Player.EventListener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            ExoPlayer.STATE_ENDED, ExoPlayer.STATE_IDLE -> {
                                handler.removeCallbacks(runnable)
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
    private fun updateSeekBar() {
        seekBar.max = exoPlayer.duration.toInt()
        seekBar.progress = exoPlayer.currentPosition.toInt()
        handler.postDelayed(runnable, 1000) // Update every second
    }
}