package pl.patrykdolata.chatapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import pl.patrykdolata.chatapp.services.SocketService

class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SocketService.connect()
        createNotificationChannel()
    }

    override fun onTerminate() {
        super.onTerminate()
        SocketService.disconnect()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "1"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}