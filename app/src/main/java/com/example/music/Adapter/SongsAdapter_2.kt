package com.example.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.Model.Song

class SongsAdapter_2(private var songs: List<Song>) : RecyclerView.Adapter<SongsAdapter_2.ViewHolder>() {

    fun setSongs(songs: List<Song>) {
        this.songs = songs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(song: Song) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
            val key = itemView.findViewById<TextView>(R.id.durationTextView)
            val descriptionTextView = itemView.findViewById<TextView>(R.id.descriptionTextView)

            Glide.with(itemView)
                .load(song.image)
                .into(imageView)
            nameTextView.text = song.title
            key.text = song.key
//            descriptionTextView.text = song.description
        }
    }
}
