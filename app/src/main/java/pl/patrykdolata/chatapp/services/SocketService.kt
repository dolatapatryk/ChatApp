package pl.patrykdolata.chatapp.services

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import pl.patrykdolata.chatapp.utils.JsonUtils
import java.net.URISyntaxException

object SocketService {
//    private const val socketUri = "http://10.0.2.2:5000"
//    private const val socketUri = "http://192.168.1.19:5000"
    private const val socketUri = "http://192.168.0.103:5000"

    private lateinit var socket: Socket;
    private lateinit var socketId: String;

    init {
        try {
            socket = IO.socket(socketUri)
        } catch (e: URISyntaxException) {
            Log.w("SocketService", e.localizedMessage!!)
        }
    }

    fun connect() {
        socket.on(Socket.EVENT_CONNECT) {
            socketId = socket.id()
            socket.off(Socket.EVENT_CONNECT)
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

    fun on(event: String, listener: Emitter.Listener) {
        socket.on(event, listener)
    }

    fun once(event: String, listener: Emitter.Listener) {
        socket.once(event, listener)
    }

    fun off(event: String) {
        socket.off(event)
    }

    fun isConnected(): Boolean {
        return socket.connected()
    }
}