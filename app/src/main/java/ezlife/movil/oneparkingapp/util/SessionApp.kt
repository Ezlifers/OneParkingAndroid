package ezlife.movil.oneparkingapp.util

import ezlife.movil.oneparkingapp.data.api.model.User


object SessionApp {
    lateinit var token: String
    lateinit var user: User
    var version:Int = 0

    fun makeToken(): String {
        return "${token}_&&_${System.currentTimeMillis()}"
    }
}