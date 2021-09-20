package com.example.diceroller_gridlayoutintro

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

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

        holder.imageView.setImageResource(diceList[position].imageIcon!!)
    }

    override fun getItemCount() = diceList.size
//    override fun getCount(): Int {
//        return diceList.size
//
//    }
//
//    override fun getItem(position: Int): Any {
//        return diceList[position]
//
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        //videos I watched on using gridViews and adapters all used safe call operators and null checks. Would appreciate a chat about that
//        val view: View = View.inflate(context, R.layout.die_result, null)
//        val imageView = view?.findViewById<ImageView>(R.id.ivDieImage)
//        imageView?.setImageResource(diceList[position].imageIcon!!)
//
//        return view!!
//    }
}