package ezlife.movil.oneparkingapp.di.modules.info

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.di.util.FragmentScope
import ezlife.movil.oneparkingapp.ui.adapter.PriceAdapter
import ezlife.movil.oneparkingapp.ui.info.prices.PricesFragment
import ezlife.movil.oneparkingapp.ui.info.prices.PricesViewModel
import javax.inject.Named

@Module
class PricesModule {

    @FragmentScope
    @Provides
    fun providePricesVieModel(fragment: PricesFragment, @Named("info") factory: ViewModelProvider.Factory): PricesViewModel
            = ViewModelProviders.of(fragment, factory).get(PricesViewModel::class.java)

    @FragmentScope
    fun provideAdapter(): PriceAdapter = PriceAdapter()
}