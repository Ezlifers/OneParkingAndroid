package ezlife.movil.oneparkingapp.ui.info.prices

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.di.Injectable
import ezlife.movil.oneparkingapp.ui.adapter.PriceAdapter
import ezlife.movil.oneparkingapp.util.LifeDisposable
import kotlinx.android.synthetic.main.fragment_prices.*
import javax.inject.Inject

class PricesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModel: PricesViewModel
    val dis: LifeDisposable = LifeDisposable(this)
    val adapter: PriceAdapter = PriceAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_prices, container, false)

    override fun onResume() {
        super.onResume()
        list.adapter = adapter
        dis add viewModel.getSetupPrice()
                .subscribe { (time, prices) ->
                    adapter.timeMin = time
                    adapter.prices = prices
                }
    }

    companion object {
        fun instance(): PricesFragment = PricesFragment()
    }
}