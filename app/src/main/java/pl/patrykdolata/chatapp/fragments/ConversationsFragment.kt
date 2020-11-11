package pl.patrykdolata.chatapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversations_fragment.view.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.activities.ConversationActivity
import pl.patrykdolata.chatapp.adapters.ConversationsAdapter
import pl.patrykdolata.chatapp.models.Conversation

class ConversationsFragment : Fragment() {

    private lateinit var conversationsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater
            .inflate(R.layout.conversations_fragment, container, false)
        initFieldsFromView(view)

        val conversationsAdapter = ConversationsAdapter(
            arrayOf(Conversation("michał", "jebać stare baby")),
        ) {
            goToConversationActivity(it)
        }
        conversationsRecyclerView.layoutManager = LinearLayoutManager(activity)
        conversationsRecyclerView.adapter = conversationsAdapter

        return view
    }

    private fun initFieldsFromView(view: View) {
        conversationsRecyclerView = view.conversationsRecyclerView
    }

    private fun goToConversationActivity(conversation: Conversation) {
        val conversationIntent = Intent(activity, ConversationActivity::class.java)
        conversationIntent.putExtra("friendName", conversation.friendName)
        Handler().postDelayed(
            { startActivity(conversationIntent) },
            10
        )
    }
}