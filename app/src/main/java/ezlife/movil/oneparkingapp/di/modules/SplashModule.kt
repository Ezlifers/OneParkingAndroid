package ezlife.movil.oneparkingapp.di.modules

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.ActivityScope
import ezlife.movil.oneparkingapp.ui.splash.SplashActivity
import ezlife.movil.oneparkingapp.ui.splash.SplashViewModel

@Module
class SplashModule {

    @ActivityScope
    @Provides
    fun provideLoginVieModel(activity: SplashActivity, factory: ViewModelProvider.Factory): SplashViewModel
            = ViewModelProviders.of(activity, factory).get(SplashViewModel::class.java)

}