package ezlife.movil.oneparkingapp.ui.cars.list


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.FragmentListCarBinding
import ezlife.movil.oneparkingapp.di.Injectable
import ezlife.movil.oneparkingapp.ui.adapter.CarAdapter
import ezlife.movil.oneparkingapp.ui.cars.CarsNavigationController
import ezlife.movil.oneparkingapp.util.LifeDisposable
import ezlife.movil.oneparkingapp.util.Loader
import ezlife.movil.oneparkingapp.util.subscribeWithError
import kotlinx.android.synthetic.main.fragment_list_car.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class ListCarFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModel: ListCarViewModel
    @Inject
    lateinit var loader: Loader
    @Inject
    lateinit var navigation: CarsNavigationController
    @Inject
    lateinit var adapter: CarAdapter
    lateinit var binding: FragmentListCarBinding

    val dis: LifeDisposable = LifeDisposable(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_car, container, false)
        binding.list.adapter = adapter
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        dis add viewModel.getCars()
                .subscribe { adapter.data = it }

        dis add btnAdd.clicks()
                .subscribe { navigation.navigateToAdd(false) }

        dis add adapter.onClearCar
                .flatMap { viewModel.deleteCar(it, adapter.data.size) }
                .subscribeWithError(
                        onNext = { toast(R.string.my_car_remove) },
                        onError = { toast(it.message!!) },
                        onHttpError = this::toast
                )

        dis add adapter.onSelectCar
                .flatMap(viewModel::selectCar)
                .subscribe()

        dis add swipe.refreshes()
                .flatMap { viewModel.reloadCars() }
                .subscribeWithError(
                        onNext = { swipe.isRefreshing = false },
                        onHttpError = this::toast
                )
    }

    companion object {
        fun instance(): ListCarFragment = ListCarFragment()
    }

}
