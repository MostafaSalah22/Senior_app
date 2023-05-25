package com.project.senior.bookingInformation.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.CategoryData
import com.project.senior.databinding.BookingInformationItemBinding

class BookingInformationAdapter :
    ListAdapter<CategoryData, BookingInformationAdapter.BookingInformationViewHolder>(
        CategoryDiffCallback()
    ) {

    var onItemClick: ((CategoryData) -> Unit)? = null

    class BookingInformationViewHolder(private val binding: BookingInformationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val title: TextView = binding.tvTitleBookingInformationItem

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingInformationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = BookingInformationItemBinding.inflate(inflater, parent, false)
        return BookingInformationViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(
        holder: BookingInformationAdapter.BookingInformationViewHolder,
        position: Int
    ) {
        val item = getItem(position)

        holder.title.text = item.title

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

}

class CategoryDiffCallback() : DiffUtil.ItemCallback<CategoryData>() {
    override fun areItemsTheSame(
        oldItem: CategoryData,
        newItem: CategoryData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryData,
        newItem: CategoryData
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}