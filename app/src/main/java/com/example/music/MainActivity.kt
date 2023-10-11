package com.example.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var homeFragment: HomeFragment
    private lateinit var settingFragment: SettingFragment
    private lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        homeFragment = HomeFragment()
        settingFragment = SettingFragment()
        searchFragment = SearchFragment()

        // Gọi phương thức startAuthorization() khi bạn muốn bắt đầu quá trình xác thực

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
        // Hiển thị com.example.music.HomeFragment ban đầu
        replaceFragment(homeFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }
}