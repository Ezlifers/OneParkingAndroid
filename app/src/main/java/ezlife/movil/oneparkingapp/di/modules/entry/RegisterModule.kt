package ezlife.movil.oneparkingapp.di.modules.entry

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.entry.register.RegisterFragment
import ezlife.movil.oneparkingapp.ui.entry.register.RegisterViewModel
import javax.inject.Named

@Module
class RegisterModule {

    @FragmentScope
    @Provides
    fun provideRegisterVieModel(fragment: RegisterFragment, @Named("entry")  factory: ViewModelProvider.Factory): RegisterViewModel
            = ViewModelProviders.of(fragment, factory).get(RegisterViewModel::class.java)

}