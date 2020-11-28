package pl.patrykdolata.chatapp.commands

import pl.patrykdolata.chatapp.services.NotificationService
import pl.patrykdolata.chatapp.utils.Constants

class FriendRequestCommand(notificationService: NotificationService) :
    FriendCommand(notificationService) {

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