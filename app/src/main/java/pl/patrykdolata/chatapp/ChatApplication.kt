package pl.patrykdolata.chatapp

import android.app.Application
import pl.patrykdolata.chatapp.services.SocketService

class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SocketService.connect()
    }
}