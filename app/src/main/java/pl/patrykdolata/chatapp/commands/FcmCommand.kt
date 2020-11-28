package pl.patrykdolata.chatapp.commands

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import pl.patrykdolata.chatapp.ChatApplication
import pl.patrykdolata.chatapp.models.FcmData

abstract class FcmCommand() {

    fun execute(context: Context, data: FcmData) {
        when (checkIfMessageIsForCurrentUser(data.toUserId)) {
            true -> executeCommand(context, data)
            false -> saveToDb(data)
        }
    }

    private fun executeCommand(context: Context, data: FcmData) {
        when (ChatApplication.isInBackground) {
            true -> executeCommandBackground(context, data)
            false -> executeCommandForeground(context, data)
        }
    }

    protected abstract fun executeCommandBackground(context: Context, data: FcmData)

    protected abstract fun executeCommandForeground(context: Context, data: FcmData)

    private fun checkIfMessageIsForCurrentUser(toUserId: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null && currentUser.uid == toUserId
    }

    private fun saveToDb(data: FcmData) {
        // todo
    }
}