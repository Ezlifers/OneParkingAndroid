package ezlife.movil.oneparkingapp.di.modules.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.main.report.ReportFragment
import ezlife.movil.oneparkingapp.ui.main.report.ReportViewModel
import javax.inject.Named

@Module
class ReportModule {

    @FragmentScope
    @Provides
    fun provideReportVieModel(fragment: ReportFragment, @Named("main") factory: ViewModelProvider.Factory): ReportViewModel
            = ViewModelProviders.of(fragment, factory).get(ReportViewModel::class.java)

}