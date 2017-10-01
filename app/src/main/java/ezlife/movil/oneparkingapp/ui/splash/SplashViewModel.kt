package ezlife.movil.oneparkingapp.ui.splash

import android.arch.lifecycle.ViewModel
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val session: UserSession) : ViewModel() {

    //TODO: Quitar esto q es de prueba
    init {
        session.setupDay = -1
    }

    fun isLogged(): Boolean = session.logged

}
