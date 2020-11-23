package pl.patrykdolata.chatapp.models

data class Conversation(val friendName: String, val lastMessage: String)

data class ConversationEntity(
    val id: Int, val userId: String, val friendId: String, val friendUsername: String
)