package com.example.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.music.Model.Song
import com.example.music.ViewModel.SongsViewModel
import com.example.music.`interface`.OnSongClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), OnSongClickListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var homeFragment: HomeFragment
    private lateinit var settingFragment: SettingFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var songsViewModel: SongsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        homeFragment = HomeFragment()
        settingFragment = SettingFragment()
        searchFragment = SearchFragment()
        songsViewModel = ViewModelProvider(this).get(SongsViewModel::class.java)

        songsViewModel.setSongClickListener(this)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(homeFragment)
                    true
                }
                R.id.navigation_search -> {
                    replaceFragment(searchFragment)
                    true
                }
                R.id.navigation_settings -> {
                    replaceFragment(settingFragment)
                    true
                }
                else -> false
            }
        }

        replaceFragment(homeFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    override fun onSongClicked(song: Song) {
        songsViewModel.setSelectedSong(song)

        val fragment = DetailFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }
}