package com.example.dice_Roller

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {


    fun pickActionBarColor(): ColorDrawable {

        return ColorDrawable(Color.parseColor("#FF000000"))

    }

}