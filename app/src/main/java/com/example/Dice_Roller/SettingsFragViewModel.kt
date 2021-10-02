package com.example.Dice_Roller

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel

class SettingsFragViewModel: ViewModel() {

    fun checkDarkModeStatus(darkModeValue: Boolean) {

        if(!darkModeValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}