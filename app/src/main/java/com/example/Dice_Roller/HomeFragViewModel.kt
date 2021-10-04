package com.example.Dice_Roller

import android.app.Application
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.example.diceroller_gridlayoutintro.R

class HomeFragViewModel(application: Application, private val resources: Resources) : AndroidViewModel(application) {

    //make this not a class level variable
    private var dieList: ArrayList<DieModel> = ArrayList()
    var howMany = 1
    var whatType = "d6"
    var sum = 0

    fun getHowManyStrArray(): Array<String> {
        return resources.getStringArray(R.array.saHowManyDice)
    }

    fun getDiceTypeStrArray(): Array<String> {
        return resources.getStringArray(R.array.saDiceType)
    }

    fun getHowMany(itemPosition: Int): Int {
        return itemPosition + 1
    }

    private fun setDiceList(diceValue: Int, diceTypeInt: Int): ArrayList<DieModel> {
    //is there another way to get package names and remove getApplication()
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

    //don't make this a function
    private fun calculateSum(diceValue: Int): Int {

        sum += diceValue
        return sum
    }

    fun getDiceValues(howMany: Int, whatType: String): ArrayList<DieModel> {

        dieList = ArrayList()
        sum = 0
        val whatTypeInt: Int = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
            dieList = setDiceList(diceValue, whatTypeInt)
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

    fun loadVibrateSetting(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(getApplication())
        return sp.getBoolean("vibrate", true)
    }

    fun checkThemeMode(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(getApplication())
        return sp.getBoolean("dark_mode", true)
    }

}