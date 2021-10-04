package com.example.Dice_Roller

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeFragViewModelFactory(private val application: Application, private val resources: Resources): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeFragViewModel(application, resources) as T
    }

}