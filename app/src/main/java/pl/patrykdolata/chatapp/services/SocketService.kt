package pl.patrykdolata.chatapp.services

import io.socket.client.IO
import io.socket.client.Socket
import pl.patrykdolata.chatapp.utils.JsonUtils
import java.net.URISyntaxException

object SocketService {
    private const val socketUri = "http://10.0.2.2:5000"
//    const val socketUri = "https://blooming-taiga-79616.herokuapp.com"

    private lateinit var socket: Socket;
    private lateinit var socketId: String;

    init {
        try {
            socket = IO.socket(socketUri)
        } catch (e: URISyntaxException) {
            println(e.localizedMessage)
        }
    }

    fun connect() {
        socket.on(Socket.EVENT_CONNECT) {
            socketId = socket.id()
        }
        socket.connect()
    }

    fun disconnect() {
        if (socket.connected()) {
            socket.disconnect()
        }
    }

    fun emit(event: String, vararg arg: Any) {
        socket.emit(event, *arg.map { a -> JsonUtils.toJson(a) }.toTypedArray())
    }

    fun getSocketId(): String {
        return socketId;
    }

    private fun isConnected(): Boolean {
        return socket.connected()
    }
}