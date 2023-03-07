package com.project.senior.seniors.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.project.domain.model.MySeniorsData
import com.project.senior.R
import com.project.senior.databinding.SeniorItemBinding

class SeniorsAdapter :
    ListAdapter<MySeniorsData, SeniorsAdapter.SeniorViewHolder>(SeniorDiffCallback()) {

    var onProfileClick: ((MySeniorsData) -> Unit)? = null
    var onDeleteClick: ((MySeniorsData) -> Unit)? = null

    class SeniorViewHolder(private val binding: SeniorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val name: TextView = binding.tvNameSeniorItem
            val age: TextView = binding.tvAgeSeniorItem
            val picture: ImageView = binding.imgPictureSeniorItem
            val delete: ImageView = binding.imgDeleteSeniorItem
            val profile: ImageView = binding.imgProfileSeniorItem

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeniorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = SeniorItemBinding.inflate(inflater, parent, false)
        return SeniorViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: SeniorViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.name
        holder.age.text = item.age.toString()
        holder.picture.load(item.image){
            placeholder(R.drawable.loading_img)
            transformations(CircleCropTransformation())
        }

        holder.profile.setOnClickListener {
            onProfileClick?.invoke(item)
        }

        holder.delete.setOnClickListener {
            onDeleteClick?.invoke(item)
        }
    }

}

class SeniorDiffCallback() : DiffUtil.ItemCallback<MySeniorsData>() {
    override fun areItemsTheSame(
        oldItem: MySeniorsData,
        newItem: MySeniorsData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MySeniorsData,
        newItem: MySeniorsData
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}