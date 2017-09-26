package ezlife.movil.oneparkingapp.di.modules.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.main.cars.CarsFragment
import ezlife.movil.oneparkingapp.ui.main.cars.CarsViewModel
import javax.inject.Named

@Module
class CarsMainModule {

    @FragmentScope
    @Provides
    fun provideCarsVieModel(fragment: CarsFragment, @Named("main") factory: ViewModelProvider.Factory): CarsViewModel
            = ViewModelProviders.of(fragment, factory).get(CarsViewModel::class.java)

}