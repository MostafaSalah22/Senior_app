package com.project.senior.schedule.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.senior.databinding.ScheduleItemBinding

class ScheduleAdapter :
    ListAdapter<ScheduleModel, ScheduleAdapter.ScheduleViewHolder>(ScheduleDiffCallback()) {

    var onItemClick: ((ScheduleModel) -> Unit)? = null

    class ScheduleViewHolder(private val binding: ScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val title: TextView = binding.tvTitleScheduleItem
            val time: TextView = binding.tvTimeScheduleItem
            val description: TextView = binding.tvDescriptionScheduleItem
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ScheduleItemBinding.inflate(inflater, parent, false)
        return ScheduleViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ScheduleAdapter.ScheduleViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.time.text = item.time
        holder.description.text = item.description

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

}

class ScheduleDiffCallback() : DiffUtil.ItemCallback<ScheduleModel>() {
    override fun areItemsTheSame(
        oldItem: ScheduleModel,
        newItem: ScheduleModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ScheduleModel,
        newItem: ScheduleModel
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}