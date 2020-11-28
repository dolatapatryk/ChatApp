package pl.patrykdolata.chatapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversations_fragment.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.activities.ConversationActivity
import pl.patrykdolata.chatapp.adapters.ConversationsAdapter
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.entitites.Conversation
import pl.patrykdolata.chatapp.viewmodels.ConversationViewModelFactory
import pl.patrykdolata.chatapp.viewmodels.ConversationsViewModel

class ConversationsFragment(private val db: AppDatabase, private val userId: String) : Fragment() {

    private lateinit var conversationsRecyclerView: RecyclerView
    private lateinit var viewModel: ConversationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater
            .inflate(R.layout.conversations_fragment, container, false)
        initFieldsFromView(view)
        conversationsRecyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel =
            ViewModelProvider(this, ConversationViewModelFactory(db.conversationDao(), userId)).get(
                ConversationsViewModel::class.java
            )
        val adapter = ConversationsAdapter(userId) { conversation ->
            goToConversationActivity(conversation)
        }
        conversationsRecyclerView.adapter = adapter
        subscribeConversations(adapter)

        return view
    }

    private fun initFieldsFromView(view: View) {
        conversationsRecyclerView = view.conversationsRecyclerView
    }

    private fun goToConversationActivity(conversation: Conversation) {
        val conversationIntent = Intent(activity, ConversationActivity::class.java)
        conversationIntent.putExtra("userId", userId)
        conversationIntent.putExtra("friendId", conversation.friendId)
        conversationIntent.putExtra("friendUsername", conversation.friendUsername)
        Handler().postDelayed(
            { startActivity(conversationIntent) },
            10
        )
    }

    private fun subscribeConversations(adapter: ConversationsAdapter) {
        viewModel.getConversations().observe(viewLifecycleOwner, { conversations ->
            println("nowe konwy")
            conversations.forEach { println(it) }
            if (conversations != null) adapter.submitList(conversations)
        })
    }
}