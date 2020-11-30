package pl.patrykdolata.chatapp.commands

import pl.patrykdolata.chatapp.crypto.KeyExchange
import pl.patrykdolata.chatapp.models.FcmData
import pl.patrykdolata.chatapp.services.NotificationService
import pl.patrykdolata.chatapp.utils.Constants
import pl.patrykdolata.chatapp.utils.JsonUtils

class FriendRequestCommand(
    notificationService: NotificationService,
    private val keyExchange: KeyExchange
) :
    FriendCommand(notificationService) {

    override fun doKeyExchangeOperations(data: FcmData) {
        val key: ByteArray? = JsonUtils.fromJson(data.text!!, ByteArray::class.java)
        keyExchange.doKeyExchangeFromSenderKey(
            key!!,
            data.toUserId,
            data.fromUserId
        )
    }

    override fun getNotificationTitle(): String {
        return "Nowe zaproszenie!"
    }

    override fun getNotificationText(friendUsername: String?): String {
        return "Nowe zaproszenie od użytkownika: $friendUsername"
    }

    override fun getSocketEvent(): String {
        return Constants.GET_FRIEND_REQUESTS_EVENT
    }
}