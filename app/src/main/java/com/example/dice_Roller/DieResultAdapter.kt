package com.example.dice_Roller

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DieResultAdapter(private var diceList: ArrayList<DieModel>): ListAdapter<DieModel, DieResultAdapter.DieModelViewHolder>(DiffCallback()) {

    class DieModelViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivDieImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieModelViewHolder {

        val view: View = View.inflate(parent.context, R.layout.die_result, null)
        return DieModelViewHolder(view)
        //Why does below not function properly?
    //        val binding = DieResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return DieModelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DieModelViewHolder, position: Int) {
        diceList[position].imageIcon?.let{
            holder.imageView.setImageResource(it)
        }
    }
    override fun getItemCount() = diceList.size

    class DiffCallback: DiffUtil.ItemCallback<DieModel>() {
        override fun areItemsTheSame(oldItem: DieModel, newItem: DieModel): Boolean {
            return oldItem == oldItem
        }

        override fun areContentsTheSame(oldItem: DieModel, newItem: DieModel): Boolean {
            return oldItem == newItem
        }

    }
}