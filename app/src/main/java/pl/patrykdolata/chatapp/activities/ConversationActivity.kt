package pl.patrykdolata.chatapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.conversation_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.MessagePagingAdapter
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.entitites.ConversationEntity
import pl.patrykdolata.chatapp.entitites.MessageEntity
import pl.patrykdolata.chatapp.viewmodels.MessageViewModel
import pl.patrykdolata.chatapp.viewmodels.MessageViewModelFactory

class ConversationActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var viewModel: MessageViewModel
    private lateinit var conversation: ConversationEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation_activity)

        db = AppDatabase(this)

        println(intent.extras!!["userId"])
        println(intent.extras!!["friendId"])

        val userId = intent.extras!!["userId"] as String
        val friendId = intent.extras!!["friendId"] as String
        val friendUsername = intent.extras!!["friendUsername"] as String

        val existingConversation =
            db.conversationDao().getByUserIdAndFriendId(userId = userId, friendId = friendId)

        conversation = if (existingConversation == null) {
            println("tworze nowa konwersacje w bazie")
            val toAdd =
                ConversationEntity(userId, friendId, friendUsername, System.currentTimeMillis(), "")
            val id = db.conversationDao().insert(toAdd)
            ConversationEntity(id, toAdd)
        } else {
            existingConversation
        }
        conversationToolbar.title = conversation.friendUsername
        val recyclerLayout = LinearLayoutManager(this)
        recyclerLayout.stackFromEnd = false
        recyclerLayout.reverseLayout = true
        messagesRecyclerView.layoutManager = recyclerLayout


        viewModel =
            ViewModelProvider(this, MessageViewModelFactory(db, conversation.id)).get(
                MessageViewModel::class.java
            )
        val adapter = MessagePagingAdapter(conversation.userId)
        messagesRecyclerView.adapter = adapter

        subscribeMessages(adapter)

        setSupportActionBar(conversationToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        sendButton.setOnClickListener {
            send()
        }
    }

    private fun subscribeMessages(adapter: MessagePagingAdapter) {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    messagesRecyclerView.layoutManager?.scrollToPosition(0)
                }
            }
        })
        viewModel.getMessages().observe(this, { messages ->
            println("nowe mesedże")
            messages.forEach { println(it) }
            if (messages != null) adapter.submitList(messages)
        })
    }

    private fun send() {
        val text = messageEditText.text.toString()
        println(text)
        viewModel.sendMessage(newMessage(text))
        // todo send event with message to server

        messageEditText.text = null
    }

    private fun newMessage(text: String): MessageEntity {
        return MessageEntity(
            conversation.id, conversation.userId, conversation.friendId,
            text, System.currentTimeMillis()
        )
    }
}
