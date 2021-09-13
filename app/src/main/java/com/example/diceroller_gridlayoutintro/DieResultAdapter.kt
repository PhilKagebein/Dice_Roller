package com.example.diceroller_gridlayoutintro

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class DieResultAdapter(var context: Context, var diceList: ArrayList<DieModel>): BaseAdapter() {
    override fun getCount(): Int {
        return diceList.size

    }

    override fun getItem(position: Int): Any {
        return diceList[position]

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View = View.inflate(context, R.layout.die_result, null)
        var imageView = view?.findViewById<ImageView>(R.id.ivDieImage)
        imageView?.setImageResource(diceList[position].imageIcon!!)

        return view!!
    }
}