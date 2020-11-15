package pl.patrykdolata.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.friends_fragment.*
import kotlinx.android.synthetic.main.friends_fragment.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.FriendsAdapter
import pl.patrykdolata.chatapp.models.Friend
import pl.patrykdolata.chatapp.services.SocketService
import pl.patrykdolata.chatapp.utils.JsonUtils
import pl.patrykdolata.chatapp.utils.SocketConstants

class FriendsFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var pendingRecyclerView: RecyclerView
    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.friends_fragment, container, false)
        initFieldsFromView(view)

        pendingRecyclerView.layoutManager = LinearLayoutManager(activity)
        friendsRecyclerView.layoutManager = LinearLayoutManager(activity)
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)

        val pendingFriendsAdapter = FriendsAdapter(listOf(Friend("1", "adam")))
        val friendsAdapter = FriendsAdapter(listOf(Friend("2", "michał")))
        val searchFriendsAdapter = FriendsAdapter(listOf())

        pendingRecyclerView.adapter = pendingFriendsAdapter
        friendsRecyclerView.adapter = friendsAdapter
        searchRecyclerView.adapter = searchFriendsAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val searchValue: String = query ?: ""
                SocketService.on(SocketConstants.FIND_USERS_RESPONSE_EVENT) { args ->
                    onFindUsersResponse(args)
                }
                SocketService.emit(SocketConstants.FIND_USERS_EVENT, userId, searchValue)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)

        return view
    }

    private fun onFindUsersResponse(responseArgs: Array<Any>) {
        SocketService.off(SocketConstants.FIND_USERS_RESPONSE_EVENT)
        activity?.runOnUiThread {
            val userList: List<Friend> = JsonUtils.fromJsonArray(responseArgs[0] as String)
            searchRecyclerView.adapter = FriendsAdapter(userList)
            noSearchResultTextView.visibility = when (userList.isEmpty()) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }
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