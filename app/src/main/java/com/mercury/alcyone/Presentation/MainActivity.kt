package com.mercury.alcyone.Presentation

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mercury.alcyone.Presentation.ViewModels.SecondSubGroupFragmentViewModel
import com.example.alcyone.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var btnNavView: BottomNavigationView
    private lateinit var controller: NavController
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: SecondSubGroupFragmentViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {

        }
        setContentView(R.layout.activity_main)
        btnNavView = findViewById(R.id.bottomNavigationView)
        controller = findNavController(R.id.fragmentContainerView)
        btnNavView.setupWithNavController(controller)


        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        
        val isNightModeOn = sharedPreferences.getBoolean("night_mode", false)

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        window.statusBarColor = Color.BLACK
    }
}