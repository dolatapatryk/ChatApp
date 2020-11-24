package pl.patrykdolata.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import pl.patrykdolata.chatapp.dao.MessageDao
import pl.patrykdolata.chatapp.entitites.MessageEntity

class MessageViewModel(private val messageDao: MessageDao, private val conversationId: Long) : ViewModel() {

    private var messages: LiveData<PagedList<MessageEntity>>

    init {
        val dataSource: DataSource.Factory<Int, MessageEntity> =
            messageDao.getConversationMessages(conversationId)
        messages = LivePagedListBuilder(dataSource, 25).build()
    }

    fun getMessages() = messages

    fun sendMessage(message: MessageEntity) = viewModelScope.launch {
        messageDao.insert(message)
    }
}

class MessageViewModelFactory(private val messageDao: MessageDao, private val conversationId: Long) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessageViewModel(messageDao, conversationId) as T
    }
}