package pl.patrykdolata.chatapp.commands

import pl.patrykdolata.chatapp.services.NotificationService
import pl.patrykdolata.chatapp.utils.Constants

class FriendAcceptedCommand(notificationService: NotificationService) :
    FriendCommand(notificationService) {

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