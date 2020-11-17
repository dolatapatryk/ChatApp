package pl.patrykdolata.chatapp.models

data class Friend(val id: String, val username: String)

enum class FriendType {
    FRIEND,
    PENDING,
    SEARCHED
}