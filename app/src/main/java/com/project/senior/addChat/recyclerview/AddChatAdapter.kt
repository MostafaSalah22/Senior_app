package com.project.senior.addChat.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.project.domain.model.AppUser
import com.project.domain.model.ChatModel
import com.project.senior.R
import com.project.senior.databinding.ChatUserItemBinding

class AddChatAdapter :
    ListAdapter<ChatModel, AddChatAdapter.AddChatViewHolder>(AddChatDiffCallback()) {

    var onItemClick: ((ChatModel) -> Unit)? = null

    class AddChatViewHolder(private val binding: ChatUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val name: TextView = binding.tvNameChatItem
        val username: TextView = binding.tvLastMessageChatItem
        val userImg: ImageView = binding.imgUserChatItem

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ChatUserItemBinding.inflate(inflater, parent, false)
        return AddChatViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: AddChatAdapter.AddChatViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.name
        holder.username.text = item.username
        holder.userImg.load(item.image){
            placeholder(R.drawable.loading_img)
            transformations(CircleCropTransformation())
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

}

class AddChatDiffCallback() : DiffUtil.ItemCallback<ChatModel>() {
    override fun areItemsTheSame(
        oldItem: ChatModel,
        newItem: ChatModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ChatModel,
        newItem: ChatModel
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}