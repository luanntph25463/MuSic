package com.example.music


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.Model.Song

// SongsAdapter.kt
class SongsAdapter(private var songs: List<Song>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, itemClickListener)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun setSongs(songs: List<Song>) {
        this.songs = songs
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(song: Song, itemClickListener: ItemClickListener) {
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
}
