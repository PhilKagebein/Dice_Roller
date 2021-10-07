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
import androidx.lifecycle.*
import androidx.preference.PreferenceManager

class HomeFragViewModel(private val app: Application, private val resources: Resources) : ViewModel() {

    val wasRollBtnPressed = MutableLiveData(false)
    var dieListLive = MutableLiveData<ArrayList<DieModel>>()
    private val sumInt = MutableLiveData(0)
    var howMany = 1
    val whatType = "d6"

    //what is the difference between the line below and creating an instance of an abstract class? Why can I do below but not lines 27/28
//    val whatType: LiveData<String>
//        get() = _whatType

    //Can't do below because LiveData is an abstract class. Cannot create an instance/object of an abstract class
//    var sumString = LiveData<String>("")

    fun getHowManyStrArray(): Array<String> {
        return resources.getStringArray(R.array.saHowManyDice)
    }

    fun getDiceTypeStrArray(): Array<String> {
        return resources.getStringArray(R.array.saDiceType)
    }

    fun getHowMany(itemPosition: Int): Int {
        howMany = itemPosition + 1
        return howMany
    }

    private fun getDiceModel(diceValue: Int, diceTypeInt: Int): DieModel {
        return DieModel(resources.getIdentifier("d${diceTypeInt}_$diceValue", "drawable", app.packageName), "d${diceTypeInt}_+$diceValue")
    }

    // @@@ktg there's probably a way to not use substring (as in, refactor how some code works
    // so that you don't need to do an operation that grabs part of a different value).
    // Using hard-coded indices should set off warning bells: it's fragile and usually not best practice.
    fun populateDiceList(howMany: Int, whatType: String) {

       val dieList = ArrayList<DieModel>()
        sumInt.value = 0
        val whatTypeInt = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
            dieList.add(getDiceModel(diceValue, whatTypeInt))
            //How do I do this without bang bang
            sumInt.value = sumInt.value!! + diceValue
        }
        dieListLive.postValue(dieList)

    }

    var sumString: LiveData<String> = sumInt.map { resources.getString(R.string.sum_text, it) }
    var sumTextVisibility: LiveData<Int> =
        wasRollBtnPressed.map {
            if (howMany == 1) {
                View.GONE
            } else {
                View.VISIBLE
            }
    }

    fun setThemeMode(darkModeValue: Boolean) {
        if(darkModeValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadVibrateSetting(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(app)
        return sp.getBoolean("vibrate", true)
    }

    fun checkThemeMode(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(app)
        return sp.getBoolean("dark_mode", true)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun vibratePhone() {
        //Is this best practice to have the function in the if statement? Or should i pull it out and set a variable equal to the return of the function and then use the variable in the if?
        if (loadVibrateSetting()) {
            val vibrator = app.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

            vibrator?.vibrate(100)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator?.vibrate(100)
            }
        }
    }

}