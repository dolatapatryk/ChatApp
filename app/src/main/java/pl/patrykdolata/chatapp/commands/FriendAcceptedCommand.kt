package pl.patrykdolata.chatapp.commands

import pl.patrykdolata.chatapp.crypto.KeyExchange
import pl.patrykdolata.chatapp.models.FcmData
import pl.patrykdolata.chatapp.services.NotificationService
import pl.patrykdolata.chatapp.utils.Constants
import pl.patrykdolata.chatapp.utils.JsonUtils

class FriendAcceptedCommand(
    notificationService: NotificationService,
    private val keyExchange: KeyExchange
) :
    FriendCommand(notificationService) {

    override fun doKeyExchangeOperations(data: FcmData) {
        val keyPair = keyExchange.getKeyPair(data.toUserId, data.fromUserId)
        val key: ByteArray? = JsonUtils.fromJson(data.text!!, ByteArray::class.java)
        val pubKey = keyExchange.getSenderPublicKey(key!!)
        keyExchange.doAgreement(keyPair, pubKey, data.toUserId, data.fromUserId)
    }

    override fun getNotificationTitle(): String {
        return "Nowy znajomy!"
    }

    override fun getNotificationText(friendUsername: String?): String {
        return "$friendUsername zaakceptował twoje zaproszenie!"
    }

    override fun getSocketEvent(): String {
        return Constants.GET_FRIENDS_EVENT
    }
}