package pl.patrykdolata.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.friends_fragment.*
import kotlinx.android.synthetic.main.friends_fragment.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.FriendsAdapter
import pl.patrykdolata.chatapp.models.Friend
import pl.patrykdolata.chatapp.models.FriendType
import pl.patrykdolata.chatapp.services.SocketService
import pl.patrykdolata.chatapp.utils.JsonUtils
import pl.patrykdolata.chatapp.utils.Constants

class FriendsFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var pendingRecyclerView: RecyclerView
    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView

    private var user: FirebaseUser? = null
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.friends_fragment, container, false)
        initFieldsFromView(view)

        user = FirebaseAuth.getInstance().currentUser
        userId = user?.uid ?: ""

        pendingRecyclerView.layoutManager = LinearLayoutManager(activity)
        friendsRecyclerView.layoutManager = LinearLayoutManager(activity)
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)

        val pendingFriendsAdapter = FriendsAdapter(listOf(), FriendType.PENDING)
        val friendsAdapter = FriendsAdapter(listOf(), FriendType.FRIEND)
        val searchFriendsAdapter = FriendsAdapter(listOf(), FriendType.SEARCHED)

        pendingRecyclerView.adapter = pendingFriendsAdapter
        friendsRecyclerView.adapter = friendsAdapter
        searchRecyclerView.adapter = searchFriendsAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchValue: String = query ?: ""
                SocketService.on(Constants.FIND_USERS_RESPONSE_EVENT) { args ->
                    onFindUsersResponse(args)
                }
                SocketService.emit(Constants.FIND_USERS_EVENT, userId, searchValue)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)

        SocketService.on(Constants.GET_FRIENDS_RESPONSE_EVENT) { args ->
            onGetFriendsResponse(args)
        }
        SocketService.on(Constants.GET_FRIEND_REQUESTS_RESPONSE_EVENT) { args ->
            onGetFriendsRequestsResponse(args)
        }
        SocketService.emit(Constants.GET_FRIENDS_EVENT, userId)
        SocketService.emit(Constants.GET_FRIEND_REQUESTS_EVENT, userId)

        return view
    }

    private fun onFindUsersResponse(responseArgs: Array<Any>) {
        SocketService.off(Constants.FIND_USERS_RESPONSE_EVENT)
        onFriendsRequestResponse(
            responseArgs, searchRecyclerView, noSearchResultTextView, false, FriendType.SEARCHED
        ) { friend ->
            sendFriendRequest(friend)
        }
    }

    private fun onGetFriendsResponse(responseArgs: Array<Any>) {
        onFriendsRequestResponse(
            responseArgs, friendsRecyclerView, friendsHeader, true, FriendType.FRIEND
        )
        val afterFriendAccept: Boolean = responseArgs[1] as Boolean
        if (afterFriendAccept) {
            SocketService.emit(Constants.GET_FRIEND_REQUESTS_EVENT, userId);
        }
    }

    private fun onGetFriendsRequestsResponse(responseArgs: Array<Any>) {
        onFriendsRequestResponse(
            responseArgs, pendingRecyclerView, pendingHeader, true, FriendType.PENDING
        ) { friend ->
            acceptFriendRequest(friend)
        }

    }

    private fun onFriendsRequestResponse(
        responseArgs: Array<Any>,
        recyclerView: RecyclerView,
        textView: TextView,
        textViewVisibilityNegation: Boolean,
        type: FriendType,
        buttonListener: (Friend) -> Unit = {}
    ) {
        activity?.runOnUiThread {
            val friends: List<Friend> = JsonUtils.fromJsonArray(responseArgs[0] as String)
            recyclerView.adapter = FriendsAdapter(friends, type, buttonListener)
            val condition =
                if (textViewVisibilityNegation) friends.isNotEmpty() else friends.isEmpty()
            textView.visibility = when (condition) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }
    }

    private fun acceptFriendRequest(friend: Friend) {
        SocketService.emit(Constants.ACCEPT_FRIEND_REQUEST_EVENT, friend.id, userId)
    }

    private fun sendFriendRequest(friend: Friend) {
        SocketService.emit(Constants.FRIEND_REQUEST_EVENT, userId, friend.id)
    }

    private fun initFieldsFromView(view: View) {
        progressBar = view.progressBar
        pendingRecyclerView = view.pendingRecyclerView
        friendsRecyclerView = view.friendsRecyclerView
        searchRecyclerView = view.searchRecyclerView
        swipeRefreshLayout = view.swipeRefreshLayout
        searchView = view.searchView
    }
}