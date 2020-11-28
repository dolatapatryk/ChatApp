package pl.patrykdolata.chatapp.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.patrykdolata.chatapp.entitites.PendingFcm.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = ["user_id"], unique = false)]
)
data class PendingFcm(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "user_id")
    var userId: String,
    @ColumnInfo(name = "from_user_id")
    var fromUserId: String,
    @ColumnInfo(name = "from_username")
    var fromUsernamr: String,
    @ColumnInfo(name = "text")
    var text: String?,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long
) {
    companion object {
        const val TABLE_NAME = "pending_fcm"
    }
}