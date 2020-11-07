package pl.patrykdolata.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.friend_item.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.models.Friend

class FriendsAdapter(private val friends: Array<Friend>) :
    RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false) as View

        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.itemView.friendName.text = friends[position].displayName
    }

    override fun getItemCount() = friends.size
}