package pl.patrykdolata.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversation_item.view.*
import kotlinx.android.synthetic.main.friend_item.view.*
import kotlinx.android.synthetic.main.friend_item.view.friendName
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.models.Conversation

class ConversationsAdapter(private val conversations: Array<Conversation>) :
    RecyclerView.Adapter<ConversationsAdapter.ConversationsViewHolder>() {

    class ConversationsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversation_item, parent, false) as View
        return ConversationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationsViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.itemView.friendName.text = conversation.friendName
        holder.itemView.lastMessage.text = conversation.lastMessage
    }

    override fun getItemCount() = conversations.size
}