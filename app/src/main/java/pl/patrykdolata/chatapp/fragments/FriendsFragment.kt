package pl.patrykdolata.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.friends_fragment.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.FriendsAdapter
import pl.patrykdolata.chatapp.models.Friend

class FriendsFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var pendingRecyclerView: RecyclerView
    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.friends_fragment, container, false)
        initFieldsFromView(view)

        progressBar.visibility = View.GONE

        pendingRecyclerView.layoutManager = LinearLayoutManager(activity)
        friendsRecyclerView.layoutManager = LinearLayoutManager(activity)
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)

        val pendingFriendsAdapter = FriendsAdapter(arrayOf(Friend("1", "adam")))
        val friendsAdapter = FriendsAdapter(arrayOf(Friend("2", "michał")))
        val searchFriendsAdapter = FriendsAdapter(arrayOf())

        pendingRecyclerView.adapter = pendingFriendsAdapter
        friendsRecyclerView.adapter = friendsAdapter
        searchRecyclerView.adapter = searchFriendsAdapter

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)

        return view
    }

    private fun initFieldsFromView(view: View) {
        progressBar = view.progressBar
        pendingRecyclerView = view.pendingRecyclerView
        friendsRecyclerView = view.friendsRecyclerView
        searchRecyclerView = view.searchRecyclerView
        swipeRefreshLayout = view.swipeRefreshLayout
    }
}