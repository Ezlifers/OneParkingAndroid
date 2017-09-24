package ezlife.movil.oneparkingapp.ui.entry.register

import android.arch.lifecycle.ViewModel
import android.content.Context
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.LoginApi
import ezlife.movil.oneparkingapp.data.api.model.RegisterReq
import ezlife.movil.oneparkingapp.data.api.model.RegisterRes
import ezlife.movil.oneparkingapp.data.api.model.User
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Observable
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val api: LoginApi,
                                            private val session: UserSession,
                                            private val context: Context) : ViewModel() {

    fun signIn(newUser: RegisterReq): Observable<Unit> {
        return api.register(newUser)
                .flatMap { saveSession(it, newUser) }
                .applyShedures()
    }

    fun validatePasswords(pass1: String, pass2: String): Observable<Unit> = Observable.create<Unit> {
        if (pass1 == pass2) it.onNext(Unit)
        else it.onError(Throwable(context.getString(R.string.reg_pass_invalid)))
        it.onComplete()
    }

    private fun saveSession(res: RegisterRes, newUser: RegisterReq): Observable<Unit> = Observable.create<Unit> {
        if (res.success) {
            val user = User(res.id, "Cliente", newUser.nombre, newUser.cedula, newUser.celular,
                    newUser.email, newUser.discapasitado, emptyList(), 0, true)
            session.initSession(res.token, user)
            it.onNext(Unit)
        } else it.onError(Throwable(
                if (res.exist) context.getString(R.string.reg_exist)
                else context.getString(R.string.reg_fail))
        )
    }

}

