package pl.patrykdolata.chatapp.entitites

import androidx.room.*
import pl.patrykdolata.chatapp.entitites.Message.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = Conversation::class,
        parentColumns = ["id"],
        childColumns = ["conversation_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("conversation_id", unique = false)]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "conversation_id")
    var conversationId: Long,
    @ColumnInfo(name = "sender_id")
    var senderId: String,
    @ColumnInfo(name = "receiver_id")
    var receiverId: String,
    @ColumnInfo(name = "text")
    var text: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long
) {
    companion object {
        const val TABLE_NAME = "message"
    }

    constructor(
        conversationId: Long,
        senderId: String,
        receiverId: String,
        text: String,
        timestamp: Long
    ) : this(0L, conversationId, senderId, receiverId, text, timestamp)
}