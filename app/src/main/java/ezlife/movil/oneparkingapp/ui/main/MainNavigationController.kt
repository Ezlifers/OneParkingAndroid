package ezlife.movil.oneparkingapp.ui.main

import android.support.v4.app.FragmentManager
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import javax.inject.Inject

@ActivityScope
class MainNavigationController @Inject constructor(private val activity: MainActivity) {

    private val idContainer = R.id.container
    private val manager: FragmentManager = activity.supportFragmentManager


}