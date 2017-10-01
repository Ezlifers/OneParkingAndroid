package ezlife.movil.oneparkingapp.di.modules.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ezlife.movil.oneparkingapp.data.observer.MarkObserver
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.di.util.ViewModelKey
import ezlife.movil.oneparkingapp.ui.main.MainActivity
import ezlife.movil.oneparkingapp.ui.main.MainViewModel
import ezlife.movil.oneparkingapp.ui.main.cars.CarsFragment
import ezlife.movil.oneparkingapp.ui.main.cars.CarsViewModel
import ezlife.movil.oneparkingapp.ui.main.report.ReportFragment
import ezlife.movil.oneparkingapp.ui.main.report.ReportViewModel
import ezlife.movil.oneparkingapp.ui.main.reserve.ReserveFragment
import ezlife.movil.oneparkingapp.ui.main.reserve.ReserveViewModel
import ezlife.movil.oneparkingapp.ui.main.setup.SetupFragment
import ezlife.movil.oneparkingapp.ui.main.setup.SetupViewModel
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneFragment
import ezlife.movil.oneparkingapp.ui.main.zone.ZoneViewModel
import ezlife.movil.oneparkingapp.util.AppViewModelFactory
import javax.inject.Named
import javax.inject.Provider

@Module
class MainModule {

    @ActivityScope
    @Provides
    @Named("main")
    fun bindViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory
            = AppViewModelFactory(creators)

    @ActivityScope
    @Provides
    fun provideMainVieModel(activity: MainActivity, factory: ViewModelProvider.Factory): MainViewModel
            = ViewModelProviders.of(activity, factory).get(MainViewModel::class.java)

    @ActivityScope
    @Provides
    fun provideMarkObserver(): MarkObserver
            = MarkObserver()


}

@Module
abstract class MainFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(CarsMainModule::class))
    abstract fun bindCarsFragment(): CarsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(ReportModule::class))
    abstract fun bindReportFragment(): ReportFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(ReserveModule::class))
    abstract fun bindReserveFragment(): ReserveFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(ZoneModule::class))
    abstract fun bindZoneFragment(): ZoneFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(SetupModule::class))
    abstract fun bindSetupFragment(): SetupFragment

}

@Module
abstract class MainViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(CarsViewModel::class)
    abstract fun bindCarsViewModel(viewModel: CarsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel::class)
    abstract fun bindReportViewModel(viewModel: ReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReserveViewModel::class)
    abstract fun bindReserveViewModel(viewModel: ReserveViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ZoneViewModel::class)
    abstract fun bindZoneViewModel(viewModel: ZoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel::class)
    abstract fun bindSetupViewModel(viewModel: SetupViewModel): ViewModel

}