package ezlife.movil.oneparkingapp.providers

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.savePreference
import ezlife.movil.oneparkingapp.db.Car
import ezlife.movil.oneparkingapp.util.Preference
import ezlife.movil.oneparkingapp.util.SessionApp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*

data class User(val _id: String, val tipo: String, val nombre: String, val cedula: String, val celular: String, val email: String, val discapasitado: Boolean, val vehiculos: List<Car>, val saldo: Int, val validado: Boolean = true)

data class LoginReq(val role: String, val user: String, val password: String, val timestamp: Long)
data class LoginRes(val success: Boolean, val timeout: Boolean, val token: String, val user: User)

data class RegisterReq(val tipo: String, val nombre: String, val cedula: String, val celular: String, val email: String, val usuario: String, val password: String, val discapasitado: Boolean)
data class RegisterRes(val success: Boolean, val token: String, val id: String, val exists: Boolean)

data class UpdatePassReq(val password: String)

interface UserService {
    @POST("usuarios/login")
    fun login(@Body req: LoginReq): Call<LoginRes>

    @POST("usuarios/signin")
    fun register(@Body req: RegisterReq): Call<RegisterRes>

    @POST("usuarios/pass")
    fun updatePassword(@Header("Authorization") auth: String, @Body req: UpdatePassReq): Call<SimpleResponse>
}

class UserProvider(val activity: AppCompatActivity, val loading: ProgressDialog? = null) {

    private val service: UserService = RetrofitHelper.retrofit.create(UserService::class.java)

    fun login(user: String, password: String, callback: (user: User) -> Unit) {
        loading?.show()
        val req = service.login(LoginReq("Cliente", user, password, Date().time))
        req.enqueue(ProviderCallback(activity, loading, R.string.login_error)
        { (success, _, token, user) ->
            if (success) {
                saveUser(token, user._id, user.nombre, user.email, user.celular, user.discapasitado)
                SessionApp.user = user
                SessionApp.token = token
                callback(user)
            }
        })
    }

    fun signin(newUser: RegisterReq, callback: (success: Boolean, exists: Boolean) -> Unit) {
        loading?.show()
        val req = service.register(newUser)
        req.enqueue(ProviderCallback(activity, loading) { (success, token, id, exists) ->
            if (success) {
                saveUser(token, id, newUser.nombre, newUser.email, newUser.celular, newUser.discapasitado)
                SessionApp.user = User(id, "Cliente", newUser.nombre, newUser.cedula, newUser.celular, newUser.email, newUser.discapasitado, emptyList(), 0)
                SessionApp.token = token
            }
            callback(success, exists)
        })
    }

    fun updatePassword(newPass:String, callback: (success: Boolean) -> Unit){
        val req = service.updatePassword(SessionApp.makeToken(), UpdatePassReq(newPass))
        req.enqueue(ProviderCallback(activity, loading) { (success) ->
            callback(success)
        })
    }

    private fun saveUser(token: String, id: String, name: String, email: String, cel: String, disability: Boolean) {
        activity.savePreference(
                Preference.TOKEN to token,
                Preference.USER_ID to id,
                Preference.NAME to name,
                Preference.USER_CEL to cel,
                Preference.USER_EMAIL to email,
                Preference.USER_DISABILITY to disability,
                Preference.ZONE_VERSION to 0
        )
    }

}
