package com.project.senior.seniorInformation.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.CategoryData
import com.project.senior.databinding.InformationItemBinding

class CategoriesAdapter :
    ListAdapter<CategoryData, CategoriesAdapter.CategoriesViewHolder>(CategoriesDiffCallback()) {

    var onItemClick: ((CategoryData) -> Unit)? = null
    var onDeleteClick: ((CategoryData) -> Unit)? = null
    var onEditClick: ((CategoryData) -> Unit)? = null

    class CategoriesViewHolder(private val binding: InformationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.tvTitleInformationItem
        val deleteInformation: ImageView = binding.imgCancelInformationItem
        val editInformation: ImageView = binding.imgEditInformationItem
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = InformationItemBinding.inflate(inflater, parent, false)
        return CategoriesViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title


        holder.title.setOnClickListener {
            onItemClick?.invoke(item)
        }

        holder.deleteInformation.setOnClickListener {
            onDeleteClick?.invoke(item)
        }

        holder.editInformation.setOnClickListener {
            onEditClick?.invoke(item)
        }
    }

}

class CategoriesDiffCallback() : DiffUtil.ItemCallback<CategoryData>() {
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