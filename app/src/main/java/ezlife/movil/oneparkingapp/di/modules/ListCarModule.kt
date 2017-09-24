package ezlife.movil.oneparkingapp.di.modules

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.FragmentScope
import ezlife.movil.oneparkingapp.ui.adapter.CarAdapter
import ezlife.movil.oneparkingapp.ui.cars.list.ListCarFragment
import ezlife.movil.oneparkingapp.ui.cars.list.ListCarViewModel
import javax.inject.Named

@Module
class ListCarModule {

    @FragmentScope
    @Provides
    fun provideListCarVieModel(fragment: ListCarFragment, @Named("cars") factory: ViewModelProvider.Factory): ListCarViewModel
            = ViewModelProviders.of(fragment, factory).get(ListCarViewModel::class.java)

    @FragmentScope
    @Provides
    fun provideAdapter(): CarAdapter = CarAdapter(true)

}