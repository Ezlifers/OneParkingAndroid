package ezlife.movil.oneparkingapp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ezlife.movil.oneparkingapp.ui.splash.SplashViewModel
import ezlife.movil.oneparkingapp.util.AppViewModelFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
abstract class ViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

}