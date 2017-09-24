package ezlife.movil.oneparkingapp.di.modules

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.FragmentScope
import ezlife.movil.oneparkingapp.ui.cars.add.AddCarFragment
import ezlife.movil.oneparkingapp.ui.cars.add.AddCarViewModel
import javax.inject.Named

@Module
class AddCarModule {

    @FragmentScope
    @Provides
    fun provideAddCarVieModel(fragment: AddCarFragment, @Named("cars") factory: ViewModelProvider.Factory): AddCarViewModel
            = ViewModelProviders.of(fragment, factory).get(AddCarViewModel::class.java)

}