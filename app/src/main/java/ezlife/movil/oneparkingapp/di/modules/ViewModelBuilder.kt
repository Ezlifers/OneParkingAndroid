package ezlife.movil.oneparkingapp.di.modules

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ezlife.movil.oneparkingapp.di.util.ViewModelKey
import ezlife.movil.oneparkingapp.ui.main.MainViewModel
import ezlife.movil.oneparkingapp.ui.splash.SplashViewModel

@Module
abstract class ViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

}