package pl.patrykdolata.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.patrykdolata.chatapp.dao.ConversationDao
import pl.patrykdolata.chatapp.entitites.ConversationEntity

class ConversationViewModel(conversationDao: ConversationDao, userId: String) : ViewModel() {

    private var conversations: LiveData<List<ConversationEntity>> =
        conversationDao.getUserConversations(userId = userId)

    fun getConversations() = conversations
}

class ConversationViewModelFactory(
    private val conversationDao: ConversationDao,
    private val userId: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConversationViewModel(conversationDao, userId) as T
    }
}