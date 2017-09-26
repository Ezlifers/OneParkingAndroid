package ezlife.movil.oneparkingapp.ui.main

import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.ui.cars.CarsActivity
import ezlife.movil.oneparkingapp.ui.main.cars.CarsFragment
import org.jetbrains.anko.startActivity
import javax.inject.Inject

@ActivityScope
class MainNavigationController @Inject constructor(val activity: MainActivity) {

    private val manager: FragmentManager = activity.supportFragmentManager

    fun showDialogCar() {
        showFragmentDialog(CarsFragment.instance())
    }

    private fun showFragmentDialog(fragment: DialogFragment) {
        val prev = manager.findFragmentByTag("dialog")
        val transaction = manager.beginTransaction()
        if (prev != null) transaction.remove(prev)
        transaction.addToBackStack(null)
        fragment.show(transaction, "dialog")
    }

    fun navigateToListCars() {
        activity.startActivity<CarsActivity>(CarsActivity.EXTRA_FIRST_TIME to false)
    }


}