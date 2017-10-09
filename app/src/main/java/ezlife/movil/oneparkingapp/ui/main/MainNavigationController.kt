package ezlife.movil.oneparkingapp.ui.main

import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import ezlife.movil.oneparkingapp.data.api.model.ZoneState
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.ui.cars.CarsActivity
import ezlife.movil.oneparkingapp.ui.infozone.InfoZoneActivity
import ezlife.movil.oneparkingapp.ui.main.cars.CarsFragment
import ezlife.movil.oneparkingapp.ui.main.report.ReportFragment
import ezlife.movil.oneparkingapp.ui.main.reserve.ReserveFragment
import ezlife.movil.oneparkingapp.ui.main.setup.SetupFragment
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneFragment
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneViewModel
import org.jetbrains.anko.startActivity
import javax.inject.Inject

@ActivityScope
class MainNavigationController @Inject constructor(val activity: MainActivity) {

    private val manager: FragmentManager = activity.supportFragmentManager

    fun showDialogCar() {
        showFragmentDialog(CarsFragment.instance())
    }

    fun showDialogSetup(version: Int) {
        showFragmentDialog(SetupFragment.instance(version))
    }

    fun showDialogZone(state: ZoneState, day:Int) {
        showFragmentDialog(ZoneFragment.instance(state, day))
    }

    fun showDialogReport(id: String, code: Int, name: String) {
        showFragmentDialog(ReportFragment.instance(id, code, name))
    }

    fun showDialogReserve(state:ZoneViewModel.State, disability:Boolean){
        showFragmentDialog(ReserveFragment.instance(state, disability))
    }

    private fun showFragmentDialog(fragment: DialogFragment) {
        val prev = manager.findFragmentByTag("dialog")
        val transaction = manager.beginTransaction()
        if (prev != null) transaction.remove(prev)
        //transaction.addToBackStack(null)
        fragment.show(transaction, "dialog")
    }

    fun navigateToListCars() {
        activity.startActivity<CarsActivity>(CarsActivity.EXTRA_FIRST_TIME to false)
    }

    fun navigateToInfo(id: String, name: String) {
        activity.startActivity<InfoZoneActivity>(InfoZoneActivity.EXTRA_ID to id,
                InfoZoneActivity.EXTRA_NAME to name)
    }


}