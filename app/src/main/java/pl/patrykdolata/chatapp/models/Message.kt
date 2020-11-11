package pl.patrykdolata.chatapp.models

data class Message(val userId: String, val message: String, val timestamp: Long)

enum class MessageType(val type: Int) {
    SENT(0),
    RECEIVED(1)
}