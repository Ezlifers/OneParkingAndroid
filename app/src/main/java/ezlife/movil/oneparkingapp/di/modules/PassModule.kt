package ezlife.movil.oneparkingapp.di.modules

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.FragmentScope
import ezlife.movil.oneparkingapp.ui.entry.pass.PassFragment
import ezlife.movil.oneparkingapp.ui.entry.pass.PassViewModel
import javax.inject.Named

@Module
class PassModule {

    @FragmentScope
    @Provides
    fun providePassVieModel(fragment: PassFragment, @Named("entry") factory: ViewModelProvider.Factory): PassViewModel
            = ViewModelProviders.of(fragment, factory).get(PassViewModel::class.java)

}