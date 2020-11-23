package pl.patrykdolata.chatapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.patrykdolata.chatapp.entitites.ConversationEntity

@Dao
interface ConversationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(conversation: ConversationEntity): Long

    @Query("delete from conversation where id = :id")
    suspend fun deleteById(id: Long)

    @Query("select * from conversation where user_id = :userId")
    fun getAllUserConversations(userId: String): LiveData<List<ConversationEntity>>

    @Query("select * from conversation where user_id = :userId and friend_id = :friendId limit 1")
    fun getByUserIdAndFriendId(userId: String, friendId: String): ConversationEntity?
}