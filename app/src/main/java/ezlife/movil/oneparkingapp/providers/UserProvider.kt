package ezlife.movil.oneparkingapp.providers

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.preferences
import ezlife.movil.oneparkingapp.util.Preference
import ezlife.movil.oneparkingapp.util.SessionApp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.*

data class Car(val apodo: String, val placa: String, val marca: String)
data class User(val _id: String, val tipo: String, val nombre: String, val cedula: String, val celular: String, val email: String, val discapasitado: Boolean, val vehiculos: List<Car>, val saldo: Int, val validado:Boolean = true)

data class LoginReq(val role: String, val user: String, val password: String, val timestamp: Long)
data class LoginRes(val success: Boolean, val timeout: Boolean, val token: String, val user: User)

data class RegisterReq(val tipo: String, val nombre: String, val cedula: String, val celular: String, val email: String, val usuario: String, val password: String, val discapasitado: Boolean)
data class RegisterRes(val success: Boolean, val token: String, val id: String, val exists: Boolean)

interface UserService {
    @POST("usuarios/login")
    fun login(@Body req: LoginReq): Call<LoginRes>

    @POST("usuarios/signin")
    fun register(@Body req: RegisterReq): Call<RegisterRes>
}

class UserProvider(val activity: AppCompatActivity, val loading: ProgressDialog? = null) {

    private val service: UserService = RetrofitHelper.retrofit.create(UserService::class.java)

    fun login(user: String, password: String, callback: (user:User) -> Unit) {
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

    private fun saveUser(token: String, id: String, name: String, email: String, cel: String, disability: Boolean) {
        val edit: SharedPreferences.Editor = activity.preferences().edit()
        edit.putBoolean(Preference.USER_LOGGED, true)
        edit.putString(Preference.TOKEN, token)
        edit.putString(Preference.USER_ID, id)
        edit.putString(Preference.NAME, name)
        edit.putString(Preference.USER_CEL, cel)
        edit.putString(Preference.USER_EMAIL, email)
        edit.putBoolean(Preference.USER_DISABILITY, disability)
        edit.apply()
    }

}
