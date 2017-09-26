package ezlife.movil.oneparkingapp.data.prefs

import android.content.SharedPreferences
import ezlife.movil.oneparkingapp.data.api.model.User
import ezlife.movil.oneparkingapp.util.Preference
import ezlife.movil.oneparkingapp.util.SessionApp
import ezlife.movil.oneparkingapp.util.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(val prefs: SharedPreferences) {

    private val token: String by lazy { prefs.getString(Preference.TOKEN, "") }
    val userID: String by lazy { prefs.getString(Preference.USER_ID, "") }
    val userName: String by lazy { prefs.getString(Preference.USER_NAME, "") }
    val userCel: String by lazy { prefs.getString(Preference.USER_CEL, "") }
    val userEmail: String by lazy { prefs.getString(Preference.USER_EMAIL, "") }
    val userDisability: String by lazy { prefs.getString(Preference.USER_DISABILITY, "") }

    var userCash: Long
        get() = prefs.getLong(Preference.USER_CASH, 0)
        set(value) = prefs.edit(Preference.USER_CASH to value)

    var logged: Boolean
        get() = prefs.getBoolean(Preference.USER_LOGGED, false)
        set(value) = prefs.edit(Preference.USER_LOGGED to value)

    var setupVersion: Int
        get() = prefs.getInt(Preference.SETUP_VERSION, 0)
        set(value) = prefs.edit(Preference.SETUP_VERSION to value)

    fun initSession(token: String, user: User) {
        prefs.edit(Preference.TOKEN to token,
                Preference.USER_ID to user._id,
                Preference.USER_NAME to user.nombre,
                Preference.USER_CEL to user.celular,
                Preference.USER_EMAIL to user.email,
                Preference.USER_DISABILITY to user.discapasitado,
                Preference.USER_CASH to user.saldo,
                Preference.SETUP_VERSION to 0)
    }

    fun makeToken(): String = "${token}_&&_${System.currentTimeMillis()}"

}