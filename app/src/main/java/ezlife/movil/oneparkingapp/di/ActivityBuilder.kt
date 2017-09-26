package ezlife.movil.oneparkingapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ezlife.movil.oneparkingapp.di.modules.SplashModule
import ezlife.movil.oneparkingapp.di.modules.cars.CarsFragmentsBuilder
import ezlife.movil.oneparkingapp.di.modules.cars.CarsModule
import ezlife.movil.oneparkingapp.di.modules.cars.CarsViewModelBuilder
import ezlife.movil.oneparkingapp.di.modules.entry.EntryFragmentsBuilder
import ezlife.movil.oneparkingapp.di.modules.entry.EntryModule
import ezlife.movil.oneparkingapp.di.modules.entry.EntryViewModelBuilder
import ezlife.movil.oneparkingapp.di.modules.main.MainFragmentsBuilder
import ezlife.movil.oneparkingapp.di.modules.main.MainModule
import ezlife.movil.oneparkingapp.di.modules.main.MainViewModelBuilder
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.ui.cars.CarsActivity
import ezlife.movil.oneparkingapp.ui.entry.EntryActivity
import ezlife.movil.oneparkingapp.ui.main.MainActivity
import ezlife.movil.oneparkingapp.ui.splash.SplashActivity

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(EntryModule::class,
            EntryFragmentsBuilder::class,
            EntryViewModelBuilder::class))
    abstract fun bindEntryActivity(): EntryActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(SplashModule::class))
    abstract fun bindSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(CarsModule::class,
            CarsFragmentsBuilder::class,
            CarsViewModelBuilder::class))
    abstract fun bindCarsActivity(): CarsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(MainModule::class,
            MainFragmentsBuilder::class,
            MainViewModelBuilder::class))
    abstract fun bindMainActivity(): MainActivity


}