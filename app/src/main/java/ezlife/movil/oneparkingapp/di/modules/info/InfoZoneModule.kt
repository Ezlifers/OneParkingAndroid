package ezlife.movil.oneparkingapp.di.modules.info

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.di.util.ViewModelKey
import ezlife.movil.oneparkingapp.ui.info.prices.PricesFragment
import ezlife.movil.oneparkingapp.ui.info.prices.PricesViewModel
import ezlife.movil.oneparkingapp.ui.infozone.schedule.ScheduleFragment
import ezlife.movil.oneparkingapp.ui.infozone.schedule.ScheduleViewModel
import ezlife.movil.oneparkingapp.util.AppViewModelFactory
import javax.inject.Named
import javax.inject.Provider

@Module
class InfoZoneModule {
    @ActivityScope
    @Provides
    @Named("info")
    fun bindViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory
            = AppViewModelFactory(creators)
}

@Module
abstract class InfoZoneFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(PricesModule::class))
    abstract fun bindPricesFragment(): PricesFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(ScheduleModule::class))
    abstract fun bindScheduleFragment(): ScheduleFragment

}

@Module
abstract class InfoZoneViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(PricesViewModel::class)
    abstract fun bindPricesViewModel(viewModel: PricesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleViewModel::class)
    abstract fun bindScheduleViewModel(viewModel: ScheduleViewModel): ViewModel
}