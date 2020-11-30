package pl.patrykdolata.chatapp.commands

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.patrykdolata.chatapp.activities.ConversationActivity
import pl.patrykdolata.chatapp.crypto.EncryptedText
import pl.patrykdolata.chatapp.crypto.MessageCrypto
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.entitites.Conversation
import pl.patrykdolata.chatapp.entitites.Message
import pl.patrykdolata.chatapp.models.FcmData
import pl.patrykdolata.chatapp.services.NotificationService
import pl.patrykdolata.chatapp.utils.JsonUtils

class NewMessageCommand(
    private val notificationService: NotificationService,
    private val db: AppDatabase,
    private val messageCrypto: MessageCrypto
) : FcmCommand() {

    override fun executeCommandBackground(context: Context, data: FcmData) {
        val x = saveNewMessage(data)
        x.invokeOnCompletion {
            val conversationIntent = Intent(context, ConversationActivity::class.java)
            conversationIntent.putExtra("userId", data.toUserId)
            conversationIntent.putExtra("friendId", data.fromUserId)
            conversationIntent.putExtra("friendUsername", data.fromUsername)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                conversationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            notificationService.createNotification(data.fromUsername, data.text, pendingIntent)
        }
        x.start()
    }

    override fun executeCommandForeground(context: Context, data: FcmData) {
        val x = saveNewMessage(data)
        x.start()
    }

    private fun saveNewMessage(data: FcmData) = GlobalScope.launch {
        val conversation = getConversationFromDb(data)
        val params = JsonUtils.fromJson(data.params!!, ByteArray::class.java)
        val encryptedText = JsonUtils.fromJson(data.text!!, ByteArray::class.java)
        val decrypted = messageCrypto.decryptMessage(
            EncryptedText(encryptedText!!, params!!),
            data.toUserId,
            data.fromUserId
        )
        val message = Message(
            conversation.id, data.fromUserId, data.toUserId, decrypted, data.timestamp
        )
        db.messageDao().insert(message)
        db.conversationDao().updateLastInteraction(conversation.id, data.timestamp, decrypted)
    }

    private suspend fun getConversationFromDb(data: FcmData): Conversation {
        val existingConversation =
            db.conversationDao()
                .getByUserIdAndFriendId(userId = data.toUserId, friendId = data.fromUserId)

        return if (existingConversation == null) {
            val toAdd =
                Conversation(
                    data.toUserId,
                    data.fromUserId,
                    data.fromUsername,
                    System.currentTimeMillis(),
                    ""
                )
            val id = db.conversationDao().insert(toAdd)
            Conversation(id, toAdd)
        } else {
            existingConversation
        }
    }
}