package pl.patrykdolata.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.entitites.Message

class MessageViewModel(private val db: AppDatabase, private val conversationId: Long) :
    ViewModel() {

    private var messages: LiveData<PagedList<Message>>

    init {
        val dataSource: DataSource.Factory<Int, Message> =
            db.messageDao().getConversationMessages(conversationId)
        messages = LivePagedListBuilder(dataSource, 100).build()
    }

    fun getMessages() = messages

    fun sendMessage(message: Message) = viewModelScope.launch {
        db.messageDao().insert(message)
        db.conversationDao().updateLastInteraction(conversationId, message.timestamp, message.text)
    }
}

class MessageViewModelFactory(private val db: AppDatabase, private val conversationId: Long) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessageViewModel(db, conversationId) as T
    }
}