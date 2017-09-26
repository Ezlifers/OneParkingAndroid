package ezlife.movil.oneparkingapp.di.modules.entry

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.entry.login.LoginFragment
import ezlife.movil.oneparkingapp.ui.entry.login.LoginViewModel
import javax.inject.Named


@Module
class LoginModule {

    @FragmentScope
    @Provides
    fun provideLoginVieModel(fragment: LoginFragment, @Named("entry") factory: ViewModelProvider.Factory): LoginViewModel
            = ViewModelProviders.of(fragment, factory).get(LoginViewModel::class.java)


}