package pl.patrykdolata.chatapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.patrykdolata.chatapp.entitites.ConversationEntity

@Dao
interface ConversationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(conversation: ConversationEntity): Long

    @Query("delete from conversation where id = :id")
    suspend fun deleteById(id: Long)

    @Query("select * from conversation where user_id = :userId order by last_interaction desc")
    fun getUserConversations(userId: String): LiveData<List<ConversationEntity>>

    @Query("select * from conversation where user_id = :userId and friend_id = :friendId limit 1")
    fun getByUserIdAndFriendId(userId: String, friendId: String): ConversationEntity?

    @Query("update conversation set last_interaction = :lastInteraction, last_message = :lastMessage where id = :conversationId")
    suspend fun updateLastInteraction(conversationId: Long, lastInteraction: Long, lastMessage: String)
}