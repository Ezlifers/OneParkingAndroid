package ezlife.movil.oneparkingapp.util

import ezlife.movil.oneparkingapp.providers.User

object SessionApp {
    lateinit var token: String
    lateinit var user: User

    fun makeToken(): String {
        return "${token}_&&_${System.currentTimeMillis()}"
    }
}