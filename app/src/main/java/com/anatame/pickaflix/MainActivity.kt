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
import android.widget.Toast
import androidx.navigation.NavController
import com.anatame.pickaflix.R
import com.anatame.pickaflix.presentation.CustomViews.ThemeableBottomNav
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentSelection by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowFlags(window)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        bottomNav.setNavBackgroundColor(Color.BLACK)
        bottomNav.selectedColor = Color.RED
        bottomNav.setNavHeight(58)
        bottomNav.setCurrentSelectedItem(0)

        currentSelection = bottomNav.currentItem


        bottomNav.setOnItemClickListener { index ->
            navigationHandler(index, navController, bottomNav)
            Toast.makeText(this,
                "Clicked item $index $currentSelection",
                Toast.LENGTH_SHORT).show()

        }
    }

private fun navigationHandler(
    index: Int,
    navController: NavController,
    bottomNav: ThemeableBottomNav
    )   {
        when (currentSelection) {
            0 -> {
                if (index == 1) {
                    navController.navigate(R.id.action_navigation_home_to_navigation_dashboard)
                    currentSelection = index
                }

                if (index == 2) {
                    navController.navigate(R.id.action_navigation_home_to_navigation_notifications)
                    currentSelection = index
                }
            }

            1 -> {
                if (index == 0) {
                    navController.navigate(R.id.action_navigation_dashboard_to_navigation_home)
                    currentSelection = index
                }

                if (index == 2) {
                    navController.navigate(R.id.action_navigation_dashboard_to_navigation_notifications)
                    currentSelection = index
                }
            }

            2 -> {
                if (index == 0) {
                    navController.navigate(R.id.action_navigation_notifications_to_navigation_home)
                    currentSelection = index
                }

                if (index == 1) {
                    navController.navigate(R.id.action_navigation_notifications_to_navigation_dashboard)
                    currentSelection = index
                }
            }
        }
    }
}

fun windowFlags(window: Window){
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.BLUE
}