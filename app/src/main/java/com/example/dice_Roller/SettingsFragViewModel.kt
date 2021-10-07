package com.example.dice_Roller

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager

class SettingsFragViewModel(application: Application) : AndroidViewModel(application) {

    fun checkAppThemeStatus(darkModeValue: Boolean) {

        if(!darkModeValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun getDarkModePreferenceStatus(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(getApplication()).getBoolean("dark_mode", true)
    }
}