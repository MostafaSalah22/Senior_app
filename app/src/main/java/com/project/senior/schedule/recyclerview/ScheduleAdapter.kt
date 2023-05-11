package com.project.senior.schedule.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.ScheduleData
import com.project.senior.R
import com.project.senior.databinding.ScheduleItemBinding
import kotlin.coroutines.coroutineContext

class ScheduleAdapter(private val context: Context) :
    ListAdapter<ScheduleData, ScheduleAdapter.ScheduleViewHolder>(ScheduleDiffCallback()) {

    var onCancelClick: ((ScheduleData) -> Unit)? = null

    class ScheduleViewHolder(private val binding: ScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val title: TextView = binding.tvTitleScheduleItem
            val time: TextView = binding.tvTimeScheduleItem
            val description: TextView = binding.tvDescriptionScheduleItem
            val cancel: Button = binding.btnCancelScheduleItem
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
        if(item.time == null){
            holder.time.text = ""
        }
        else holder.time.text = item.time
        if(item.description == null){
            holder.description.text = context.getString(R.string.no_description)
        }
        else holder.description.text = item.description.toString()

        holder.cancel.setOnClickListener {
            onCancelClick?.invoke(item)
        }
    }

}

class ScheduleDiffCallback() : DiffUtil.ItemCallback<ScheduleData>() {
    override fun areItemsTheSame(
        oldItem: ScheduleData,
        newItem: ScheduleData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ScheduleData,
        newItem: ScheduleData
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}