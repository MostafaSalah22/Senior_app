package com.project.senior.messages.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.model.Message
import com.project.senior.databinding.ReceiveItemBinding
import com.project.senior.databinding.SentItemBinding

class MessagesAdapter(private val sentId: String) :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    //var onItemClick: ((Message) -> Unit)? = null

    class SentViewHolder(private val binding: SentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val sentMessage = binding.tvSentMessage
    }

    class ReceiveViewHolder(private val binding: ReceiveItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val receiveMessage = binding.tvReceiveMessage
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if(viewType == 1){
            val inflater = LayoutInflater.from(parent.context)
            val listItemBinding = ReceiveItemBinding.inflate(inflater, parent, false)
            ReceiveViewHolder(listItemBinding)
        } else{
            val inflater = LayoutInflater.from(parent.context)
            val listItemBinding = SentItemBinding.inflate(inflater, parent, false)
            SentViewHolder(listItemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = getItem(position)

        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else{
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }


        /*val item = getItem(position)


        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }*/
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = getItem(position)
        return if(sentId == currentMessage.senderId)
            ITEM_SENT
        else
            ITEM_RECEIVE
    }

}

class MessageDiffCallback() : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(
        oldItem: Message,
        newItem: Message
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Message,
        newItem: Message
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}