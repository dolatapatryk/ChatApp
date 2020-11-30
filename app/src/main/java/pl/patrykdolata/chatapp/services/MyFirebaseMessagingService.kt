package pl.patrykdolata.chatapp.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykdolata.chatapp.ChatApplication
import pl.patrykdolata.chatapp.commands.*
import pl.patrykdolata.chatapp.crypto.KeyExchange
import pl.patrykdolata.chatapp.crypto.MessageCrypto
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.models.FcmData
import pl.patrykdolata.chatapp.utils.Constants
import pl.patrykdolata.chatapp.utils.JsonUtils
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var keyExchange: KeyExchange

    @Inject
    lateinit var messageCrypto: MessageCrypto
    private lateinit var db: AppDatabase
    private var messageReceivers: Map<String, FcmCommand> = mapOf()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            SocketService.emit(Constants.REFRESH_TOKEN_EVENT, user.uid, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        println("dostałem mesydż, że trzeba robić rzeczy")
        println("isInBackground? = " + ChatApplication.isInBackground)
        initializeDb()
        initializeCommands()
        if (message.data.isNotEmpty()) {
            val data = JsonUtils.mapToModel(message.data, FcmData::class.java)
            if (data != null) {
                val command: FcmCommand? = messageReceivers[data.type]
                command?.execute(this, data)
            } else {
                return
            }
        }
    }

    private fun initializeCommands() {
        if (messageReceivers.isEmpty()) {
            messageReceivers = mapOf(
                Constants.NEW_MESSAGE_TYPE to NewMessageCommand(
                    notificationService,
                    db,
                    messageCrypto
                ),
                Constants.FRIEND_REQUEST_TYPE to FriendRequestCommand(
                    notificationService,
                    keyExchange
                ),
                Constants.FRIEND_ACCEPTED_TYPE to FriendAcceptedCommand(
                    notificationService,
                    keyExchange
                ),
                Constants.KEY_EXCHANGE_REQUEST_TYPE to KeyExchangeRequestCommand(notificationService)
            )
        }
    }

    private fun initializeDb() {
        if (!::db.isInitialized) {
            db = AppDatabase(this)
        }
    }
}