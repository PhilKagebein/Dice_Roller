package com.example.Dice_Roller

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.diceroller_gridlayoutintro.R

class DieResultAdapter(private var context: Context, private var diceList: ArrayList<DieModel>):
    RecyclerView.Adapter<DieResultAdapter.DieResultViewHolder>() {

    class DieResultViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivDieImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieResultViewHolder {
        val view: View = View.inflate(context, R.layout.die_result, null)

        return DieResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: DieResultViewHolder, position: Int) {

        diceList[position].imageIcon?.let{
            holder.imageView.setImageResource(it)
        }
    }

    override fun getItemCount() = diceList.size
}