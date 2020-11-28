package pl.patrykdolata.chatapp.commands

import android.content.Context
import pl.patrykdolata.chatapp.models.FcmData
import pl.patrykdolata.chatapp.services.NotificationService

class KeyExchangeRequestCommand(private val notificationService: NotificationService) :
    FcmCommand(notificationService) {

    override fun executeCommandBackground(context: Context, data: FcmData) {
        TODO("Not yet implemented")
    }

    override fun executeCommandForeground(context: Context, data: FcmData) {
        TODO("Not yet implemented")
    }
}