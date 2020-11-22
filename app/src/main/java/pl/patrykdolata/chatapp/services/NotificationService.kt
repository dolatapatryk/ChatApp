package pl.patrykdolata.chatapp.services

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import pl.patrykdolata.chatapp.ChatApplication.Companion.NOTIFICATION_CHANNEL_ID
import pl.patrykdolata.chatapp.R
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(@ApplicationContext private val context: Context) {

    fun createNotification(title: String?, text: String?, pendingIntent: PendingIntent) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(Objects.hash(title, text), builder.build())
        }
    }
}