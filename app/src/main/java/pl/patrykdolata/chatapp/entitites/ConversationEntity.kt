package pl.patrykdolata.chatapp.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.patrykdolata.chatapp.entitites.ConversationEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(value = ["user_id"], unique = false),
        Index(value = ["user_id", "friend_id"], unique = true)
    ]
)
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "user_id")
    var userId: String,
    @ColumnInfo(name = "friend_id")
    var friendId: String,
    @ColumnInfo(name = "friend_username")
    var friendUsername: String
) {
    companion object {
        const val TABLE_NAME = "conversation"
    }

    constructor(userId: String, friendId: String, friendUsername: String) : this(
        0L,
        userId,
        friendId,
        friendUsername
    )

    constructor(id: Long, conversation: ConversationEntity) : this(
        id,
        conversation.userId,
        conversation.friendId,
        conversation.friendUsername
    )
}