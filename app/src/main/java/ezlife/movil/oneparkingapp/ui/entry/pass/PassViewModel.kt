package ezlife.movil.oneparkingapp.ui.entry.pass

import android.arch.lifecycle.ViewModel
import android.content.Context
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.LoginApi
import ezlife.movil.oneparkingapp.data.api.model.SimpleResponse
import ezlife.movil.oneparkingapp.data.api.model.UpdatePassReq
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Observable
import javax.inject.Inject

class PassViewModel @Inject constructor(private val api: LoginApi,
                                        private val context: Context,
                                        private val session: UserSession) : ViewModel() {

    fun updatePass(newPass: String): Observable<SimpleResponse>
            = api.updatePassword(session.makeToken(), UpdatePassReq(newPass))
            .flatMap(this::validateSuccess)
            .applyShedures()

    private fun validateSuccess(res: SimpleResponse): Observable<SimpleResponse> = Observable.create {
        if (res.success) it.onNext(res)
        else it.onError(Throwable(context.getString(R.string.pass_error_2)))
    }

    fun validatePasswords(pass1: String, pass2: String): Observable<Unit> = Observable.create<Unit> {
        if (pass1 == pass2) it.onNext(Unit)
        else it.onError(Throwable(context.getString(R.string.reg_pass_invalid)))
        it.onComplete()
    }

}