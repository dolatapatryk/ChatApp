package pl.patrykdolata.chatapp.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.conversation_activity.*
import kotlinx.coroutines.launch
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.MessagePagingAdapter
import pl.patrykdolata.chatapp.crypto.EncryptedMessage
import pl.patrykdolata.chatapp.crypto.MessageCrypto
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.entitites.Conversation
import pl.patrykdolata.chatapp.entitites.Message
import pl.patrykdolata.chatapp.models.Friend
import pl.patrykdolata.chatapp.services.SocketService
import pl.patrykdolata.chatapp.utils.Constants
import pl.patrykdolata.chatapp.utils.JsonUtils
import pl.patrykdolata.chatapp.viewmodels.MessageViewModel
import pl.patrykdolata.chatapp.viewmodels.MessageViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class ConversationActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "ConversationActivity"
    }

    private lateinit var db: AppDatabase
    private lateinit var viewModel: MessageViewModel
    private lateinit var conversation: Conversation

    @Inject
    lateinit var messageCrypto: MessageCrypto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation_activity)

        db = AppDatabase(this)

        Log.d(TAG, "${intent.extras!!["userId"]}")
        Log.d(TAG, "${intent.extras!!["friendId"]}")

        val userId = intent.extras!!["userId"] as String
        val friendId = intent.extras!!["friendId"] as String
        val friendUsername = intent.extras!!["friendUsername"] as String

        lifecycleScope.launch {
            getConversation(userId, Friend(friendId, friendUsername))
        }

        val recyclerLayout = LinearLayoutManager(this)
        recyclerLayout.stackFromEnd = false
        recyclerLayout.reverseLayout = true
        messagesRecyclerView.layoutManager = recyclerLayout
        messagesRecyclerView.adapter = MessagePagingAdapter("")
        setSupportActionBar(conversationToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        sendButton.setOnClickListener {
            send()
        }
    }

    private suspend fun getConversation(userId: String, friend: Friend) {
        conversation = getConversationFromDb(userId, friend)

        conversationToolbar.title = conversation.friendUsername
        viewModel =
            ViewModelProvider(this, MessageViewModelFactory(db, conversation.id)).get(
                MessageViewModel::class.java
            )
        val adapter = MessagePagingAdapter(conversation.userId)
        messagesRecyclerView.adapter = adapter

        subscribeMessages(adapter)
    }

    private suspend fun getConversationFromDb(userId: String, friend: Friend): Conversation {
        val existingConversation =
            db.conversationDao().getByUserIdAndFriendId(userId = userId, friendId = friend.id)

        return if (existingConversation == null) {
            val toAdd =
                Conversation(
                    userId,
                    friend.id,
                    friend.username,
                    System.currentTimeMillis(),
                    ""
                )
            val id = db.conversationDao().insert(toAdd)
            Conversation(id, toAdd)
        } else {
            existingConversation
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
            if (messages != null) adapter.submitList(messages)
        })
    }

    private fun send() {
        if (SocketService.isConnected()) {
            val text = messageEditText.text.toString()
            val encryptedText =
                messageCrypto.encryptMessage(text, conversation.userId, conversation.friendId)
            val messageToSend = EncryptedMessage(
                conversation.userId,
                conversation.friendId,
                JsonUtils.toJson(encryptedText.text),
                JsonUtils.toJson(encryptedText.params),
                System.currentTimeMillis()
            )
            val message = newMessage(text)
            SocketService.once(Constants.NEW_MESSAGE_RESPONSE_EVENT) { args ->
                onNewMessageResponse(args, message)
            }
            SocketService.emit(Constants.NEW_MESSAGE_EVENT, messageToSend)
        } else {
            makeMessageSendFailedToast()
        }

    }

    private fun onNewMessageResponse(args: Array<Any>, newMessage: Message) {
        val result = args[0] as Int
        if (result == 1) {
            viewModel.sendMessage(newMessage)
            runOnUiThread { messageEditText.text = null }
        } else {
            makeMessageSendFailedToast()
        }
    }

    private fun newMessage(text: String): Message {
        return Message(
            conversation.id, conversation.userId, conversation.friendId,
            text, System.currentTimeMillis()
        )
    }

    private fun makeMessageSendFailedToast() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(
                this, "Nie udało się wysłać wiadomości. Brak połączenia!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
