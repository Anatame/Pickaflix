package com.anatame.pickaflix

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.anatame.pickaflix.databinding.ActivityMainBinding
import android.view.WindowManager

import android.os.Build
import android.view.Window
import com.anatame.pickaflix.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowFlags(window)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav = binding.bottomNav

        bottomNav.setNavBackgroundColor(Color.BLACK)
        bottomNav.selectedColor = Color.RED
        bottomNav.setNavHeight(48)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

    }
}

fun windowFlags(window: Window){
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.BLUE
}