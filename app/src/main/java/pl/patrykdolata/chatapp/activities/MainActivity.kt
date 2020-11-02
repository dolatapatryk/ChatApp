package pl.patrykdolata.chatapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.Socket
import kotlinx.android.synthetic.main.main_activity.*
import pl.patrykdolata.chatapp.R

class MainActivity : AppCompatActivity() {

    lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        connectButton.setOnClickListener {
//            try {
////                socket = IO.socket("http://10.0.2.2:5000")
//                socket = IO.socket("https://blooming-taiga-79616.herokuapp.com")
//            } catch (e: Exception) {
//                println(e.message)
//            }
//            socket.on(Socket.EVENT_CONNECT) {
//                println("connected");
//                println(socket.connected())
//                socket.emit("test", "patryk connected")
//            }
//            socket.connect()
        }

        emitButton.setOnClickListener {
//            socket.emit("test", "patryk")
//            socket.emit("test2", "patryk2")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}
