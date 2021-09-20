package com.example.diceroller_gridlayoutintro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class DieResultAdapter(var context: Context, var diceList: ArrayList<DieModel>):
    RecyclerView.Adapter<DieResultAdapter.DieResultViewHolder>() {

    class DieResultViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivDieImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieResultViewHolder {
        val view: View = View.inflate(context, R.layout.die_result, null)

        return DieResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: DieResultViewHolder, position: Int) {
        // @@@ktg
        // Bang bang (!!) is something we very much try to avoid. Couple ways to avoid it.
        // You could make imageIcon a non-nullable property of DieModel
        // You could unwrap the nullable using let, i.e.
        /*
        diceList[position].imageIcon?.let {
            holder.imageView.setImageResource(it)
        }
        */
        // We can chat about unwrapping, super important in Kotlin

        holder.imageView.setImageResource(diceList[position].imageIcon!!)
    }

    override fun getItemCount() = diceList.size
    // @@@ktg
    // Version control should empower you to just delete code rather than commenting it out
    // since you can reference previous versions of your code if you ever need old code
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