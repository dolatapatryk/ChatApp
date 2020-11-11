package pl.patrykdolata.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sent_message_item.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.models.Message
import pl.patrykdolata.chatapp.models.MessageType
import java.lang.IllegalArgumentException

class MessageAdapter(private val messages: Array<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].userId == "1")
            MessageType.SENT.type
        else
            MessageType.RECEIVED.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MessageType.SENT.type -> SentMessageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.sent_message_item, parent, false))
            MessageType.RECEIVED.type -> ReceivedMessageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.received_message_item, parent, false))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.timestampTextView.text = messages[position].timestamp.toString()
        holder.itemView.messageTextView.text = messages[position].message
    }

    override fun getItemCount() = messages.size
}