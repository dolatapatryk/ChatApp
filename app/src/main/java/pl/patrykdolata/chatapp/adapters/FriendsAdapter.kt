package pl.patrykdolata.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.friend_item.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.models.Friend
import pl.patrykdolata.chatapp.models.FriendType

class FriendsAdapter(
    private val friends: List<Friend>,
    private val friendType: FriendType,
    private val listener: (Friend) -> Unit = {}
) :
    RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false) as View

        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.itemView.friendName.text = friends[position].username
        when (friendType) {
            FriendType.FRIEND -> {
                holder.itemView.inviteButton.visibility = View.GONE
                holder.itemView.setOnClickListener { listener(friends[position]) }
            }
            FriendType.SEARCHED -> holder.itemView.inviteButton.text = "Zaproś"
            FriendType.PENDING -> holder.itemView.inviteButton.text = "Akceptuj"
        }
        holder.itemView.inviteButton.setOnClickListener { listener(friends[position]) }
    }

    override fun getItemCount() = friends.size
}