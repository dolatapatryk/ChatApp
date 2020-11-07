package pl.patrykdolata.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversations_fragment.view.*
import pl.patrykdolata.chatapp.R
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
            arrayOf(Conversation("michał", "jebać stare baby"))
        )
        conversationsRecyclerView.layoutManager = LinearLayoutManager(activity)
        conversationsRecyclerView.adapter = conversationsAdapter

        return view
    }

    private fun initFieldsFromView(view: View) {
        conversationsRecyclerView = view.conversationsRecyclerView
    }
}