package pl.patrykdolata.chatapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.patrykdolata.chatapp.entitites.MessageEntity

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: MessageEntity): Long

    @Query("delete from message where id = :id")
    suspend fun deleteById(id: Long)

    @Query("select * from message where conversation_id = :conversationId")
    fun getAllConversationMessages(conversationId: Long): LiveData<List<MessageEntity>>
}