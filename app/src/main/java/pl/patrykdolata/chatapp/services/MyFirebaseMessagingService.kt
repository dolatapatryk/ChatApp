package pl.patrykdolata.chatapp.services

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.patrykdolata.chatapp.activities.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationService = NotificationService(this)

    override fun onMessageReceived(message: RemoteMessage) {
        println("dostałem mesydż, że trzeba robić rzeczy")
        if (message.data.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
            notificationService.createNotification(
                message.data["type"], message.data["message"],
                pendingIntent
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {

    }
}