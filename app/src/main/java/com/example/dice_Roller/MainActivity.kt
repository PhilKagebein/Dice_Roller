package com.example.dice_Roller

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dice_Roller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var displayOverflowMenu = true
    private val viewModel: MainViewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        setupActionBarWithNavController(findNavController(R.id.main_fragment))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.topbar_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.settings -> {
                displayOverflowMenu = false
                findNavController(R.id.main_fragment).navigate(R.id.navigateToSettingsFragment)
                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if(displayOverflowMenu){
            displayOverflowMenu = false
            return true
        }else {
            displayOverflowMenu = true
            return false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        displayOverflowMenu = true
        invalidateOptionsMenu()
        val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setActionBar() {

        val actionBar = supportActionBar
        actionBar?.elevation = ACTIONBAR_ELEVATION
        actionBar?.setBackgroundDrawable(viewModel.pickActionBarColor())
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    companion object{
        const val ACTIONBAR_ELEVATION = 15.0F
    }
}


