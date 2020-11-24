package pl.patrykdolata.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversation_item.view.*
import kotlinx.android.synthetic.main.friend_item.view.friendName
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.entitites.ConversationEntity
import pl.patrykdolata.chatapp.utils.StringUtils

class ConversationsAdapter(val userId: String, private val listener: (ConversationEntity) -> Unit) :
    ListAdapter<ConversationEntity, ConversationsAdapter.ConversationsViewHolder>(
        ConversationDiffCallback()
    ) {

    class ConversationsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversation_item, parent, false) as View
        return ConversationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationsViewHolder, position: Int) {
        val conversation = getItem(position)
        if (conversation == null) {
            holder.itemView.friendName.text = null
            holder.itemView.lastMessage.text = null
            holder.itemView.dateTextView.text = null
        } else {
            holder.itemView.friendName.text = conversation.friendUsername
            holder.itemView.lastMessage.text = conversation.lastMessage
            holder.itemView.dateTextView.text = StringUtils.formatDate(conversation.lastInteraction)
            holder.itemView.setOnClickListener { listener(conversation) }
        }

    }
}

class ConversationDiffCallback : DiffUtil.ItemCallback<ConversationEntity>() {
    override fun areItemsTheSame(
        oldItem: ConversationEntity,
        newItem: ConversationEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ConversationEntity,
        newItem: ConversationEntity
    ): Boolean {
        return oldItem == newItem
    }
}