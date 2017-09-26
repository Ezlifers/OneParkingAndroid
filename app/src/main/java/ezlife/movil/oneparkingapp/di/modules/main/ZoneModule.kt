package ezlife.movil.oneparkingapp.di.modules.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneFragment
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneViewModel
import javax.inject.Named

@Module
class ZoneModule {

    @FragmentScope
    @Provides
    fun provideZoneVieModel(fragment: ZoneFragment, @Named("main") factory: ViewModelProvider.Factory): ZoneViewModel
            = ViewModelProviders.of(fragment, factory).get(ZoneViewModel::class.java)

}