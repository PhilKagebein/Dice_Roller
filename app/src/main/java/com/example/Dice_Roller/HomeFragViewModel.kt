package com.example.Dice_Roller

import android.app.Application
import android.content.res.Resources
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.diceroller_gridlayoutintro.R

class HomeFragViewModel : ViewModel() {

    fun getHowManyStrArray(resources: Resources): Array<String> {
        return resources.getStringArray(R.array.saHowManyDice)
    }

    fun getDiceTypeStrArray(resources: Resources): Array<String> {
        return resources.getStringArray(R.array.saDiceType)
    }


}