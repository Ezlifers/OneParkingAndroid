package ezlife.movil.oneparkingapp.di.modules.cars

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ezlife.movil.oneparkingapp.data.api.CarApi
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.di.util.ViewModelKey
import ezlife.movil.oneparkingapp.ui.cars.add.AddCarFragment
import ezlife.movil.oneparkingapp.ui.cars.add.AddCarViewModel
import ezlife.movil.oneparkingapp.ui.cars.list.ListCarFragment
import ezlife.movil.oneparkingapp.ui.cars.list.ListCarViewModel
import ezlife.movil.oneparkingapp.util.AppViewModelFactory
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Provider

@Module
class CarsModule {

    @ActivityScope
    @Provides
    fun provideCarApi(retrofit: Retrofit): CarApi
            = retrofit.create(CarApi::class.java)

    @ActivityScope
    @Provides
    @Named("cars")
    fun bindViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory
            = AppViewModelFactory(creators)

}

@Module
abstract class CarsFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(AddCarModule::class))
    abstract fun bindAddCarFragment(): AddCarFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(ListCarModule::class))
    abstract fun bindListCarFragment(): ListCarFragment

}

@Module
abstract class CarsViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(AddCarViewModel::class)
    abstract fun bindAddCarViewModel(viewModel: AddCarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListCarViewModel::class)
    abstract fun bindListCArViewModel(viewModel: ListCarViewModel): ViewModel

}