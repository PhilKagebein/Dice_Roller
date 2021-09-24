package com.example.Dice_Roller

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEachIndexed
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.diceroller_gridlayoutintro.R
import com.example.diceroller_gridlayoutintro.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var displayOverflowMenu = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        val actionBarColor = ColorDrawable(Color.parseColor("#191919"))
        actionBar?.setBackgroundDrawable(actionBarColor)
        actionBar?.setDisplayShowTitleEnabled(false)

// both options work
//        val navController = Navigation.findNavController(this,R.id.main_fragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)
      setupActionBarWithNavController(findNavController(R.id.main_fragment))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.topbar_menu, menu)
        return true

    }
    //why only work in Main Activity? -because onOptionsItemSelected is part of the activity class yes?
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.settings -> {
                displayOverflowMenu = false
                invalidateOptionsMenu()
                findNavController(R.id.main_fragment).navigate(R.id.navigateToSettingsFragment)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if(!displayOverflowMenu){
            displayOverflowMenu = true
            return false
        }else {
            return true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        invalidateOptionsMenu()
        val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}


