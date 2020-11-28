package pl.patrykdolata.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sent_message_item.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.entitites.Message
import pl.patrykdolata.chatapp.utils.StringUtils

class MessagePagingAdapter(val userId: String) :
    PagedListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.senderId == userId)
            MessageType.SENT.type
        else
            MessageType.RECEIVED.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MessageType.SENT.type -> SentMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sent_message_item, parent, false)
            )
            MessageType.RECEIVED.type -> ReceivedMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.received_message_item, parent, false)
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (message == null) {
            holder.itemView.timestampTextView.text = null
            holder.itemView.messageTextView.text = null
        } else {
            holder.itemView.timestampTextView.text = StringUtils.formatDate(message.timestamp)
            holder.itemView.messageTextView.text = message.text
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}

enum class MessageType(val type: Int) {
    SENT(0),
    RECEIVED(1)
}
