package com.example.Dice_Roller

import android.app.Application
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.example.diceroller_gridlayoutintro.R

class HomeFragViewModel(application: Application) : AndroidViewModel(application) {

    private var dieList: ArrayList<DieModel> = ArrayList()
    var howMany = 1
    var whatType = "d6"
    var sum = 0

    fun getHowManyStrArray(resources: Resources): Array<String> {
        return resources.getStringArray(R.array.saHowManyDice)
    }

    fun getDiceTypeStrArray(resources: Resources): Array<String> {
        return resources.getStringArray(R.array.saDiceType)
    }

    fun getHowMany(itemPosition: Int): Int {
        return itemPosition + 1
    }

    private fun setDiceList(resources: Resources, diceValue: Int, diceTypeInt: Int): ArrayList<DieModel> {

        dieList.add(DieModel(resources.getIdentifier("d${diceTypeInt}_$diceValue", "drawable", getApplication<Application>().packageName), "d${diceTypeInt}_+$diceValue"))

        return dieList
    }

    fun setSumVisibility(howMany: Int, whatType: String): Int {
        if (howMany == 0 || howMany == 1 || whatType == "") {
            return View.GONE
        } else {
            return View.VISIBLE
        }
    }

    private fun calculateSum(diceValue: Int): Int {

        sum += diceValue
        return sum
    }

    fun getDiceValues(resources: Resources, howMany: Int, whatType: String): ArrayList<DieModel> {

        dieList = ArrayList()
        sum = 0
        val whatTypeInt: Int = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
            dieList = setDiceList(resources, diceValue, whatTypeInt)
            sum = calculateSum(diceValue)
        }

        return dieList
    }

    fun returnSumText() : String {
        return "Sum: $sum"
    }

    fun resetSumText(): String {
        return ""
    }

    fun setThemeMode(darkModeValue: Boolean) {
        if(darkModeValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}