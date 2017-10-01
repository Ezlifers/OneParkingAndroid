package ezlife.movil.oneparkingapp.ui.entry.login


import android.arch.lifecycle.ViewModel
import android.content.Context
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.LoginApi
import ezlife.movil.oneparkingapp.data.api.model.LoginReq
import ezlife.movil.oneparkingapp.data.api.model.LoginRes
import ezlife.movil.oneparkingapp.data.api.model.User
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val carDao: CarDao,
                                         private val api: LoginApi,
                                         private val context: Context,
                                         private val session: UserSession) : ViewModel() {

    fun login(username: String, password: String): Observable<User>
            = api.login(LoginReq("Cliente", username, password, Date().time))
            .flatMap(this::saveSession)
            .flatMap { user ->
                if (user.vehiculos.isNotEmpty()) user.vehiculos[0].selected = true
                Observable.fromCallable { carDao.insertList(user.vehiculos) }
                        .map { user }
            }.applyShedures()


    private fun saveSession(res: LoginRes): Observable<User> = Observable.create<User> {
        if (res.success) {
            session.initSession(res.token, res.user)
            it.onNext(res.user)
        } else it.onError(Throwable(
                if (res.timeout) context.getString(R.string.login_error_timeout)
                else context.getString(R.string.login_error_unknown))
        )
    }


}