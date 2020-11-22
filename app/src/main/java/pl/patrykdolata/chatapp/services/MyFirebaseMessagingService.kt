package pl.patrykdolata.chatapp.services

import android.app.PendingIntent
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykdolata.chatapp.ChatApplication
import pl.patrykdolata.chatapp.activities.MainActivity
import pl.patrykdolata.chatapp.utils.Constants
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationService: NotificationService

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            SocketService.emit(Constants.REFRESH_TOKEN_EVENT, user.uid, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        println("dostałem mesydż, że trzeba robić rzeczy")
        println("isInBackground? = " + ChatApplication.isInBackground)
        if (message.data.isNotEmpty()) {
            val data = message.data
            when (data["type"]) {
                Constants.NEW_MESSAGE_TYPE -> handleNewMessage(data)
                Constants.FRIEND_REQUEST_TYPE -> handleNewFriendRequest(data)
                Constants.FRIEND_ACCEPTED_TYPE -> handleFriendAccepted(data)
                Constants.KEY_EXCHANGE_REQUEST_TYPE -> handleKeyExchangeRequest(data)
            }
        }
    }

    private fun handleNewMessage(data: Map<String, String>) {

    }

    private fun handleNewFriendRequest(data: Map<String, String>) {
        val text = "Nowe zaproszenie od użytkownika: " + data["fromUsername"]
        handleFriendTypeCloudMessage(
            data,
            text,
            "Nowe zaproszenie!",
            Constants.GET_FRIEND_REQUESTS_EVENT
        )
    }

    private fun handleFriendAccepted(data: Map<String, String>) {
        val text = data["fromUsername"] + " zaakceptował twoje zaproszenie!"
        handleFriendTypeCloudMessage(data, text, "Nowy znajomy!", Constants.GET_FRIENDS_EVENT)
    }

    private fun handleFriendTypeCloudMessage(
        data: Map<String, String>, text: String,
        title: String, event: String
    ) {
        if (checkIfMessageIsForCurrentUser(data)) {
            if (ChatApplication.isInBackground) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragment", "friends")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                val pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
                notificationService.createNotification(title, text, pendingIntent)
            } else {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
                }
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                SocketService.emit(event, userId)
            }
        } else {
            saveToDb(data)
        }
    }

    private fun handleKeyExchangeRequest(data: Map<String, String>) {

    }

    private fun checkIfMessageIsForCurrentUser(data: Map<String, String>): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null && currentUser.uid == data["toUserId"]
    }

    // TODO
    private fun saveToDb(data: Map<String, String>) {

    }
}