package pl.patrykdolata.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.patrykdolata.chatapp.dao.ConversationDao
import pl.patrykdolata.chatapp.entitites.Conversation

class ConversationsViewModel(conversationDao: ConversationDao, userId: String) : ViewModel() {

    private var conversations: LiveData<List<Conversation>> =
        conversationDao.getUserConversations(userId = userId)

    fun getConversations() = conversations
}

class ConversationViewModelFactory(
    private val conversationDao: ConversationDao,
    private val userId: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConversationsViewModel(conversationDao, userId) as T
    }
}