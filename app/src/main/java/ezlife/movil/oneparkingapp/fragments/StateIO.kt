package ezlife.movil.oneparkingapp.fragments

import android.content.Context
import com.google.gson.Gson
import ezlife.movil.oneparkingapp.R
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class StateIO(val context: Context, val id: String) {

    companion object {
        val RESERVE = 0
        val END_RESERVE = 1
    }

    private val gson = Gson()
    private val url: String by lazy { context.getString(R.string.url_io) + "socket/global" }

    private val socket: Socket by lazy {
        val options = IO.Options()
        options.reconnection = true
        options.forceNew = true
        IO.socket(url, options)
    }

    fun connect(callback: (id: String, type: Int, dis: Boolean) -> Unit) {
        socket.on(Socket.EVENT_CONNECT, {
            socket.emit("subscribe")
        }).on("global_state") { data ->
            val json = data[0] as JSONObject
            val state = gson.fromJson(json.toString(), StateMsg::class.java)
            callback(state.id, state.type, state.dis)
        }
    }

    fun disconnect() {
        if (socket.connected()) {
            socket.emit("unsubscribe")
            socket.disconnect()
        }
    }

}

private data class StateMsg(val id: String, val type: Int, val dis: Boolean)