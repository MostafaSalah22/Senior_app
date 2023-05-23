package com.project.senior.chat.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.project.domain.model.ChatModel
import com.project.senior.R
import com.project.senior.databinding.ChatUserItemBinding

class ChatAdapter : ListAdapter<ChatModel, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    var onItemClick: ((ChatModel) -> Unit)? = null

    class ChatViewHolder(private val binding: ChatUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val name:TextView = binding.tvNameChatItem
            val lastMessage:TextView = binding.tvLastMessageChatItem
            val userImg:ImageView = binding.imgUserChatItem
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ChatUserItemBinding.inflate(inflater, parent, false)
        return ChatViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        val item = getItem(position)
        holder.name.text = item.name
        holder.lastMessage.text = item.lastMessage
        //holder.userImg.setImageResource(R.drawable.me)
        holder.userImg.load(R.drawable.baseline_account_circle_24){
            transformations(CircleCropTransformation())
        }


        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

}

class ChatDiffCallback() : DiffUtil.ItemCallback<ChatModel>() {
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