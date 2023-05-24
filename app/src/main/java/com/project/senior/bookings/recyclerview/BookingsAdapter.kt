package com.project.senior.bookings.recyclerview

import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.BookingsDetails
import com.project.senior.databinding.BookingsItemBinding

class BookingsAdapter :
    ListAdapter<BookingsDetails, BookingsAdapter.BookingsViewHolder>(BookingsDiffUtilDiffCallback()) {

    var onCancelClick: ((BookingsDetails) -> Unit)? = null
    var onDetailsClick: ((BookingsDetails) -> Unit)? = null

    class BookingsViewHolder(private val binding: BookingsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val name: TextView = binding.tvNameBookingsItem
            val date: TextView = binding.tvDateBookingsItem
            val cancel: Button = binding.btnCancelBookingsItem
            val details: ImageView = binding.imgDetailsBookings

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = BookingsItemBinding.inflate(inflater, parent, false)
        return BookingsViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: BookingsAdapter.BookingsViewHolder, position: Int) {
        val item = getItem(position)
        holder.name.text = item.senior.name
        holder.date.text = item.date

        holder.cancel.setOnClickListener {
            onCancelClick?.invoke(item)
        }

        holder.details.setOnClickListener {
            onDetailsClick?.invoke(item)
        }
    }

}

class BookingsDiffUtilDiffCallback() : DiffUtil.ItemCallback<BookingsDetails>() {
    override fun areItemsTheSame(
        oldItem: BookingsDetails,
        newItem: BookingsDetails
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BookingsDetails,
        newItem: BookingsDetails
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}