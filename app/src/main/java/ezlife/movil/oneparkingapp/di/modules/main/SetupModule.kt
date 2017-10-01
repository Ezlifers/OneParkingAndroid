package ezlife.movil.oneparkingapp.di.modules.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.main.report.ReportFragment
import ezlife.movil.oneparkingapp.ui.main.report.ReportViewModel
import ezlife.movil.oneparkingapp.ui.main.setup.SetupFragment
import ezlife.movil.oneparkingapp.ui.main.setup.SetupViewModel
import javax.inject.Named

@Module
class SetupModule {

    @FragmentScope
    @Provides
    fun provideSetupVieModel(fragment: SetupFragment, @Named("main") factory: ViewModelProvider.Factory): SetupViewModel
            = ViewModelProviders.of(fragment, factory).get(SetupViewModel::class.java)

}