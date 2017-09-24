package ezlife.movil.oneparkingapp.ui.entry

import android.support.v4.app.FragmentManager
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.AddCarActivity
import ezlife.movil.oneparkingapp.activities.MapActivity
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.di.ActivityScope
import ezlife.movil.oneparkingapp.ui.entry.login.LoginFragment
import ezlife.movil.oneparkingapp.ui.entry.pass.PassFragment
import ezlife.movil.oneparkingapp.ui.entry.register.RegisterFragment
import org.jetbrains.anko.startActivity
import javax.inject.Inject

@ActivityScope
class EntryNavigationController @Inject constructor(private val activity: EntryActivity,
                                                    private val session: UserSession) {

    private val idContainer = R.id.container
    private val manager: FragmentManager = activity.supportFragmentManager

    fun navigateToLogin() {
        manager.beginTransaction()
                .replace(idContainer, LoginFragment.instance())
                .addToBackStack(null)
                .commit()
    }

    fun navigateToRegister() {
        manager.beginTransaction()
                .replace(idContainer, RegisterFragment.instance())
                .addToBackStack(null)
                .commit()
    }

    fun navigateToPass(hasCars: Boolean) {
        manager.beginTransaction()
                .replace(idContainer, PassFragment.instance(hasCars))
                .addToBackStack(null)
                .commit()
    }

    fun navigateToAddCar() {
        activity.startActivity<AddCarActivity>(AddCarActivity.EXTRA_FIRST_TIME to true)
        activity.finish()
    }

    fun navigateToMap() {
        session.logged = true
        activity.startActivity<MapActivity>()
        activity.finish()
    }


}