package pl.patrykdolata.chatapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.conversation_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.MessageAdapter
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.entitites.ConversationEntity
import pl.patrykdolata.chatapp.entitites.MessageEntity

class ConversationActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var conversation: ConversationEntity? = null

    private val changeObserver = Observer<List<MessageEntity>> { value ->
        value?.let { messages ->
            val messageAdapter = MessageAdapter(messages, conversation!!.userId)
            messagesRecyclerView.adapter = messageAdapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation_activity)

        db = AppDatabase(this)

        println(intent.extras!!["userId"])
        println(intent.extras!!["friendId"])

        val userId = intent.extras!!["userId"] as String
        val friendId = intent.extras!!["friendId"] as String
        val friendUsername = intent.extras!!["friendUsername"] as String

        conversation =
            db.conversationDao().getByUserIdAndFriendId(userId = userId, friendId = friendId)

        if (conversation == null) {
            println("tworze nowa konwersacje w bazie")
            val toAdd = ConversationEntity(userId, friendId, friendUsername)
            val id = db.conversationDao().insert(toAdd)
            conversation = ConversationEntity(id, toAdd)
        }
        conversationToolbar.title = conversation!!.friendUsername

        messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        setSupportActionBar(conversationToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        sendButton.setOnClickListener {
            send()
        }

        val messageAdapter = MessageAdapter(listOf(), conversation!!.userId)
        messagesRecyclerView.adapter = messageAdapter

        db.messageDao().getAllConversationMessages(conversation!!.id)
            .observe(this, changeObserver)
    }

    private fun send() {
        println(messageEditText.text)
    }
}
