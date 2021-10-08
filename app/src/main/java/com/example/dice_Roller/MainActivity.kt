package com.example.dice_Roller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dice_Roller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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

    override fun onSupportNavigateUp(): Boolean {
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


