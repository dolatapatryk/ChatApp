package pl.patrykdolata.chatapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.patrykdolata.chatapp.dao.ConversationDao
import pl.patrykdolata.chatapp.dao.MessageDao
import pl.patrykdolata.chatapp.entitites.ConversationEntity
import pl.patrykdolata.chatapp.entitites.MessageEntity

@Database(
    entities = [ConversationEntity::class, MessageEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "ChatApp.db"
        ).allowMainThreadQueries().build()
    }
}