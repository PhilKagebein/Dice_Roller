package com.example.Dice_Roller

import androidx.recyclerview.widget.GridLayoutManager

class DiceSpanLookup(dieList: ArrayList<DieModel>): GridLayoutManager.SpanSizeLookup() {

    private var dieListSize = dieList.size

    override fun getSpanSize(position: Int): Int {

        when(dieListSize) {
            4, 6 -> {return 1}
            5 -> {
                if(position == 2){
                    return 2
                }else {
                    return 1
                }
            }
        }
        return 2
    }
}