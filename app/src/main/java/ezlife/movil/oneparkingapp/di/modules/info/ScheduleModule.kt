package ezlife.movil.oneparkingapp.di.modules.info

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.adapter.ScheduleAdapter
import ezlife.movil.oneparkingapp.ui.infozone.schedule.ScheduleFragment
import ezlife.movil.oneparkingapp.ui.infozone.schedule.ScheduleViewModel
import javax.inject.Named

@Module
class ScheduleModule {

    @FragmentScope
    @Provides
    fun provideScheduleVieModel(fragment: ScheduleFragment, @Named("info") factory: ViewModelProvider.Factory): ScheduleViewModel
            = ViewModelProviders.of(fragment, factory).get(ScheduleViewModel::class.java)

    @FragmentScope
    fun provideAdapter(): ScheduleAdapter = ScheduleAdapter()
}