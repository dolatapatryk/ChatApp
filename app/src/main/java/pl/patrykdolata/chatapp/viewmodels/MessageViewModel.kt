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
import pl.patrykdolata.chatapp.entitites.MessageEntity

class MessageViewModel(private val db: AppDatabase, private val conversationId: Long) :
    ViewModel() {

    private var messages: LiveData<PagedList<MessageEntity>>

    init {
        val dataSource: DataSource.Factory<Int, MessageEntity> =
            db.messageDao().getConversationMessages(conversationId)
        messages = LivePagedListBuilder(dataSource, 100).build()
    }

    fun getMessages() = messages

    fun sendMessage(message: MessageEntity) = viewModelScope.launch {
        db.messageDao().insert(message)
        db.conversationDao().updateLastInteraction(
            conversationId, System.currentTimeMillis(),
            message.text
        )
    }
}

class MessageViewModelFactory(private val db: AppDatabase, private val conversationId: Long) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessageViewModel(db, conversationId) as T
    }
}