package com.example.music.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.Model.Song
import com.example.music.R
import com.example.music.SongsAdapter

class SearchAdapter(private var songs: List<Song>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, itemClickListener)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(song: Song, itemClickListener: SearchAdapter.ItemClickListener) {
            Glide.with(itemView)
                .load(song.image)
                .into(imageView)
            nameTextView.text = song.title

            itemView.setOnClickListener {
                itemClickListener.onItemClick(song)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(song: Song)
    }

    fun setSongs(songs: List<Song>) {
        this.songs = songs
    }
}