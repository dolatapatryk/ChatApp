package pl.patrykdolata.chatapp.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.patrykdolata.chatapp.entitites.Message

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: Message): Long

    @Query("delete from message where id = :id")
    suspend fun deleteById(id: Long)

    @Query("select * from message where conversation_id = :conversationId order by timestamp desc")
    fun getConversationMessages(conversationId: Long): DataSource.Factory<Int, Message>
}