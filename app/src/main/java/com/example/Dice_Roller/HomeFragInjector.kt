package com.example.Dice_Roller

import android.app.Application
import android.content.res.Resources

class HomeFragInjector {

    object Injector{

        fun provideHomeFragViewModelFactory(application: Application, resources: Resources): HomeFragViewModelFactory {
            return HomeFragViewModelFactory(application, resources)
        }
    }
}