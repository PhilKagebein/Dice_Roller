package com.example.dice_Roller

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel: SettingsFragViewModel by lazy{
        ViewModelProvider(this)[SettingsFragViewModel::class.java]
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val darkMode = findPreference<Preference>("dark_mode")
        darkMode?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
                _, _ -> changeThemeMode()
        }
    }

    @SuppressLint("ResourceType")
    private fun changeThemeMode(): Boolean{

        val darkModeValue = settingsViewModel.getDarkModePreferenceStatus()
        settingsViewModel.checkAppThemeStatus(darkModeValue)

        return true
    }

}

