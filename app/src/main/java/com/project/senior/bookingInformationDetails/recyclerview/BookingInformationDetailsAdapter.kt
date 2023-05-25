package com.project.senior.bookingInformationDetails.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.CategoryDetailsData
import com.project.senior.databinding.BookingInformationDetailsItemBinding

class BookingInformationDetailsAdapter :
    ListAdapter<CategoryDetailsData, BookingInformationDetailsAdapter.BookingInformationDetailsViewHolder>(
        CategoryDetailsDiffCallback()
    ) {


    class BookingInformationDetailsViewHolder(private val binding: BookingInformationDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title: TextView = binding.tvTitleBookingInformationDetailsItem
        val description: TextView = binding.tvDescriptionBookingInformationDetailsItem
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingInformationDetailsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = BookingInformationDetailsItemBinding.inflate(inflater, parent, false)
        return BookingInformationDetailsViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(
        holder: BookingInformationDetailsAdapter.BookingInformationDetailsViewHolder,
        position: Int
    ) {
        val item = getItem(position)

        holder.title.text = item.title
        holder.description.text = item.description

    }

}

class CategoryDetailsDiffCallback() : DiffUtil.ItemCallback<CategoryDetailsData>() {
    override fun areItemsTheSame(
        oldItem: CategoryDetailsData,
        newItem: CategoryDetailsData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryDetailsData,
        newItem: CategoryDetailsData
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}