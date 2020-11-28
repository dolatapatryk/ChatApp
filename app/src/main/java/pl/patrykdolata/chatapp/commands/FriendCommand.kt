package pl.patrykdolata.chatapp.commands

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import pl.patrykdolata.chatapp.activities.MainActivity
import pl.patrykdolata.chatapp.models.FcmData
import pl.patrykdolata.chatapp.services.NotificationService
import pl.patrykdolata.chatapp.services.SocketService

abstract class FriendCommand(private val notificationService: NotificationService) : FcmCommand() {

    override fun executeCommandBackground(context: Context, data: FcmData) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("fragment", "friends")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        notificationService.createNotification(
            getNotificationTitle(),
            getNotificationText(data.fromUsername),
            pendingIntent
        )
    }

    override fun executeCommandForeground(context: Context, data: FcmData) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(context, getNotificationText(data.fromUsername), Toast.LENGTH_LONG)
                .show()
        }
        SocketService.emit(getSocketEvent(), data.toUserId)
    }

    protected abstract fun getNotificationTitle(): String

    protected abstract fun getNotificationText(friendUsername: String?): String

    protected abstract fun getSocketEvent(): String
}