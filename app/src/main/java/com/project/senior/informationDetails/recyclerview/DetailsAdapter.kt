package com.project.senior.informationDetails.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.CategoryData
import com.project.domain.model.CategoryDetailsData
import com.project.senior.databinding.InformationDetailsItemBinding

class DetailsAdapter :
    ListAdapter<CategoryDetailsData, DetailsAdapter.DetailsViewHolder>(DetailsDiffCallback()) {

    //var onItemClick: ((CategoryDetailsData) -> Unit)? = null
    var onDeleteClick: ((CategoryDetailsData) -> Unit)? = null
    var onEditClick: ((CategoryDetailsData) -> Unit)? = null

    class DetailsViewHolder(private val binding: InformationDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title: TextView = binding.tvTitleInformationDetailsItem
        val description: TextView = binding.tvDescriptionInformationDetailsItem
        val deleteInformation: ImageView = binding.imgCancelInformationDetailsItem
        val editInformation: ImageView = binding.imgEditInformationDetailsItem
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = InformationDetailsItemBinding.inflate(inflater, parent, false)
        return DetailsViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: DetailsAdapter.DetailsViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.description.text = item.description

        holder.deleteInformation.setOnClickListener {
            onDeleteClick?.invoke(item)
        }

        holder.editInformation.setOnClickListener {
            onEditClick?.invoke(item)
        }
    }

}

class DetailsDiffCallback() : DiffUtil.ItemCallback<CategoryDetailsData>() {
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