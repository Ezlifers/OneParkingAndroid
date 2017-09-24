package ezlife.movil.oneparkingapp.ui.cars

import android.support.v4.app.FragmentManager
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.di.ActivityScope
import ezlife.movil.oneparkingapp.ui.cars.add.AddCarFragment
import ezlife.movil.oneparkingapp.ui.cars.list.ListCarFragment
import javax.inject.Inject

@ActivityScope
class CarsNavigationController @Inject constructor(private val activity: CarsActivity) {

    private val idContainer = R.id.container
    private val manager: FragmentManager = activity.supportFragmentManager

    fun navigateToAdd(firstTime: Boolean) {
        if(!firstTime)
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
                .addToBackStack(null)
                .commit()
    }

    fun goToBack(firstTime: Boolean) {
        if(firstTime) activity.finish()
        else activity.onBackPressed()
    }

}