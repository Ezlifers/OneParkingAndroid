package ezlife.movil.oneparkingapp.ui.cars

import android.support.v4.app.FragmentManager
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.ui.cars.add.AddCarFragment
import ezlife.movil.oneparkingapp.ui.cars.list.ListCarFragment
import ezlife.movil.oneparkingapp.ui.main.MainActivity
import org.jetbrains.anko.startActivity
import javax.inject.Inject

@ActivityScope
class CarsNavigationController @Inject constructor(private val activity: CarsActivity,
                                                   private val session: UserSession) {

    private val idContainer = R.id.container
    private val manager: FragmentManager = activity.supportFragmentManager

    fun navigateToAdd(firstTime: Boolean) {
        if (!firstTime)
            activity.supportActionBar?.setTitle(R.string.title_activity_add_car)
        manager.beginTransaction()
                .replace(idContainer, AddCarFragment.instance(firstTime))
                .addToBackStack(null)
                .commit()
    }

    fun navigateToList() {
        activity.supportActionBar?.setTitle(R.string.title_activity_my_cars)
        manager.beginTransaction()
                .replace(idContainer, ListCarFragment.instance())
                .commit()
    }

    fun navigateToMain() {
        session.logged = true
        activity.startActivity<MainActivity>()
        activity.finish()
    }

    fun goToBack(firstTime: Boolean) {
        if (firstTime) activity.finish()
        else activity.onBackPressed()
    }

}