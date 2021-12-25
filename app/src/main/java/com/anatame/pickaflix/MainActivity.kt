package com.anatame.pickaflix

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import android.view.WindowManager

import android.view.Window
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.anatame.pickaflix.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import android.content.res.ColorStateList
import com.anatame.pickaflix.presentation.Fragments.Detail.MovieDetailFragment


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    val context: Context = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowFlags(context, window)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search,  R.id.navigation_saved, R.id.navigation_settings
            )
        )


        navView.setupWithNavController(navController)

        val iconColorStates = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                ContextCompat.getColor(this, R.color.UnselectedIconColor),
                ContextCompat.getColor(this, R.color.PrimaryAccent)
            )
        )

        navView.setItemIconTintList(iconColorStates)
        navView.setItemTextColor(iconColorStates)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()

        return super.onSupportNavigateUp()
    }

}


fun windowFlags(context: Context, window: Window){
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(context, R.color.BackgroundDark);
}