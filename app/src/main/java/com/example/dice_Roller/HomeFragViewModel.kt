package com.example.dice_Roller

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class HomeFragViewModel(application: Application, private val resources: Resources) : AndroidViewModel(application) {
//hey change this so I don't have to use AndroidViewModel

    private val _howMany = MutableLiveData<Int>(1)
    val howMany: LiveData<Int>
        get() = _howMany

    private val _whatType = MutableLiveData<String>("d6")
    val whatType: LiveData<String>
        get() = _whatType

 /*   val whatType = thingIDependOn.map {
        //stuff
        value
    }*/

    //Could move _sum into fun populateDiceList() but does livedata need to be a member variable?
    private val _sum = MutableLiveData(0)
    val sum: LiveData<Int>
        get() = _sum

    fun getHowManyStrArray(): Array<String> {
        return resources.getStringArray(R.array.saHowManyDice)
    }

    fun getDiceTypeStrArray(): Array<String> {
        return resources.getStringArray(R.array.saDiceType)
    }

    fun getHowMany(itemPosition: Int): Int? {
        _howMany.value = itemPosition + 1
        return howMany.value
    }

    private fun getDiceModel(diceValue: Int, diceTypeInt: Int): DieModel {
    //is there another way to get package names and remove getApplication()
    return DieModel(resources.getIdentifier("d${diceTypeInt}_$diceValue", "drawable", getApplication<Application>().packageName), "d${diceTypeInt}_+$diceValue")
    }

    fun setSumVisibility(howMany: Int, whatType: String): Int {
        if (howMany == 0 || howMany == 1 || whatType == "") {
            return View.GONE
        } else {
            return View.VISIBLE
        }
    }

    // dumbSum is just the number we want to display
  /*  val sum = dumbSum.map { dumbSum ->
        resources.getString(R.string.sum_text, dumbSum)
    }*/

    // @@@ktg there's probably a way to not use substring (as in, refactor how some code works
    // so that you don't need to do an operation that grabs part of a different value).
    // Using hard-coded indices should set off warning bells: it's fragile and usually not best practice.
    fun populateDiceList(howMany: Int, whatType: String): ArrayList<DieModel> {

        val dieList = ArrayList<DieModel>()
   /*     _sum.value = 0
        val whatTypeInt: Int = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
            dieList.add(getDiceModel(diceValue, whatTypeInt))
            //How do I do this
            _sum.value = _sum.value!! + diceValue
            //sum += diceValue
        }*/

        return dieList
    }

    fun getDiceTypeString(itemPosition: Int, strArray: Array<String>): String? {
        _whatType.value = strArray[itemPosition]
        return whatType.value
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

    private fun loadVibrateSetting(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(getApplication())
        return sp.getBoolean("vibrate", true)
    }

    fun checkThemeMode(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(getApplication())
        return sp.getBoolean("dark_mode", true)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun vibratePhone() {
        //Is this best practice to have the function in the if statement? Or should i pull it out and set a variable equal to the return of the function and then use the variable in the if?
        if (loadVibrateSetting()) {
            val vibrator = getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

            vibrator?.vibrate(100)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator?.vibrate(100)
            }
        }
    }

}