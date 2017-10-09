package ezlife.movil.oneparkingapp.ui.info.prices

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.ui.adapter.PriceAdapter
import ezlife.movil.oneparkingapp.util.push
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_prices.*
import javax.inject.Inject

class PricesFragment : Fragment() {

    @Inject
    lateinit var viewModel: PricesViewModel
    val dis: CompositeDisposable = CompositeDisposable()
    val adapter: PriceAdapter = PriceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_prices, container, false)

    override fun onResume() {
        super.onResume()
        list.adapter = adapter
        dis push viewModel.getSetupPrice()
                .subscribe { (time, prices) ->
                    adapter.timeMin = time
                    adapter.prices = prices
                }
    }

    override fun onStop() {
        super.onStop()
        dis.clear()
    }

    companion object {
        fun instance(): PricesFragment = PricesFragment()
    }
}