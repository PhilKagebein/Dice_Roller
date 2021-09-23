package com.example.Dice_Roller

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.diceroller_gridlayoutintro.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}