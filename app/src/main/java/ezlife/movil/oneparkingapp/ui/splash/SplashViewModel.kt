package ezlife.movil.oneparkingapp.ui.splash

import android.arch.lifecycle.ViewModel
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.di.ActivityScope
import javax.inject.Inject

class SplashViewModel @Inject constructor(val session: UserSession) : ViewModel() {

    fun isLogged(): Boolean = session.logged

}
