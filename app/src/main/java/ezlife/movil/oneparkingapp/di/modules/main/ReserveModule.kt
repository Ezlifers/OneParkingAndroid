package ezlife.movil.oneparkingapp.di.modules.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.main.reserve.ReserveFragment
import ezlife.movil.oneparkingapp.ui.main.reserve.ReserveViewModel
import javax.inject.Named

@Module
class ReserveModule {

    @FragmentScope
    @Provides
    fun providePassVieModel(fragment: ReserveFragment, @Named("main") factory: ViewModelProvider.Factory): ReserveViewModel
            = ViewModelProviders.of(fragment, factory).get(ReserveViewModel::class.java)

}