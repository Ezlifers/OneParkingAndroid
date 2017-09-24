package ezlife.movil.oneparkingapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ezlife.movil.oneparkingapp.di.modules.*
import ezlife.movil.oneparkingapp.ui.cars.CarsActivity
import ezlife.movil.oneparkingapp.ui.entry.EntryActivity
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

}