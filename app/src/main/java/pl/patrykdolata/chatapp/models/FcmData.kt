package pl.patrykdolata.chatapp.models

open class FcmData(
    val type: String, val fromUserId: String, val fromUsername: String, val toUserId: String,
    val text: String?, val timestamp: Long
)