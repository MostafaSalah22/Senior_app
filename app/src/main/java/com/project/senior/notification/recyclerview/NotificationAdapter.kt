package com.project.senior.notification.recyclerview


import com.project.senior.databinding.NotificationItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter :
    ListAdapter<NotificationModel, NotificationAdapter.NotificationViewHolder>(
        NotificationDiffCallback()
    ) {

    var onItemClick: ((NotificationModel) -> Unit)? = null

    class NotificationViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val title: TextView = binding.tvTitleNotificationItem
            val message: TextView = binding.tvMessageNotificationItem

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = NotificationItemBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.message.text = item.message

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

}

class NotificationDiffCallback() : DiffUtil.ItemCallback<NotificationModel>() {
    override fun areItemsTheSame(
        oldItem: NotificationModel,
        newItem: NotificationModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: NotificationModel,
        newItem: NotificationModel
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}