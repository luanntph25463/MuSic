package com.example.music.Model

data class Song(
    val id: Int, // Thêm cột id tự tăng
    val key: String,
    val title: String,
    val image: String,
    val href: String,
){
    companion object {
        private var nextId = 1 // Biến tĩnh để tự động tăng giá trị id

        fun getNextId(): Int {
            return nextId++
        }
    }
}