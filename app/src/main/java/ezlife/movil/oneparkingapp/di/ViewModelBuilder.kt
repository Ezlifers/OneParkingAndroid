package ezlife.movil.oneparkingapp.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ezlife.movil.oneparkingapp.di.util.ViewModelKey
import ezlife.movil.oneparkingapp.ui.splash.SplashViewModel

@Module
abstract class ViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

}