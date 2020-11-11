package pl.patrykdolata.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.conversation_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.MessageAdapter
import pl.patrykdolata.chatapp.models.Message

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation_activity)

        println(intent.extras!!["friendName"])

        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        conversationToolbar.title = "Michał"

        setSupportActionBar(conversationToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        sendButton.setOnClickListener {
            send()
        }

        val messages = arrayOf(
            Message("2", "Co tam?", 123),
            Message("1", "Wszystko ok, a tam?", 132),
            Message("2", "Też dobrze?", 155)
        )
        val messageAdapter = MessageAdapter(messages)
        messagesRecyclerView.adapter = messageAdapter
    }

    private fun send() {
        println(messageEditText.text)
    }
}
