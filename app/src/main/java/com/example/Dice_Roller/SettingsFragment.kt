package com.example.Dice_Roller

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.diceroller_gridlayoutintro.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsViewModel: SettingsFragViewModel by lazy{
        ViewModelProvider(this)[SettingsFragViewModel::class.java]
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        var darkMode = findPreference<Preference>("dark_mode")
        darkMode?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
                darkMode, _ -> changeThemeMode()
        }

        }

    @SuppressLint("ResourceType")
    private fun changeThemeMode(): Boolean{
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val darkModeValue = sp.getBoolean("dark_mode", true)

        settingsViewModel.checkDarkModeStatus(darkModeValue)

        return true
    }



}

