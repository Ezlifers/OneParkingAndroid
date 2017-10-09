package ezlife.movil.oneparkingapp.ui.main.cars

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.FragmentCarsBinding
import ezlife.movil.oneparkingapp.di.Injectable
import ezlife.movil.oneparkingapp.ui.adapter.CarAdapter
import ezlife.movil.oneparkingapp.ui.main.MainNavigationController
import ezlife.movil.oneparkingapp.util.LifeDisposable
import kotlinx.android.synthetic.main.fragment_cars.*
import javax.inject.Inject


class CarsFragment : DialogFragment(), Injectable {
    @Inject
    lateinit var viewModel: CarsViewModel
    @Inject
    lateinit var adapter: CarAdapter
    @Inject
    lateinit var navigation: MainNavigationController
    lateinit var binding: FragmentCarsBinding

    val dis: LifeDisposable = LifeDisposable(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cars, container, false)
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        dis add btnSetup.clicks()
                .subscribe {
                    navigation.navigateToListCars()
                    dismiss()
                }

        dis add viewModel.getCars()
                .subscribe { adapter.data = it }

        dis add adapter.onSelectCar
                .flatMap(viewModel::selectCar)
                .subscribe()
    }

    companion object {
        fun instance() = CarsFragment()
    }

}
