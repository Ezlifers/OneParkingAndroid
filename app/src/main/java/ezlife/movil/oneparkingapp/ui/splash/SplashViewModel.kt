package ezlife.movil.oneparkingapp.ui.splash

import android.arch.lifecycle.ViewModel
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val session: UserSession) : ViewModel() {

    fun isLogged(): Boolean = session.logged

}
