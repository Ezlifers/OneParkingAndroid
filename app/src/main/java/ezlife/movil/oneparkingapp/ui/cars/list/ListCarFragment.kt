package ezlife.movil.oneparkingapp.ui.cars.list


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.FragmentListCarBinding
import ezlife.movil.oneparkingapp.ui.adapter.CarAdapter
import ezlife.movil.oneparkingapp.ui.cars.CarsNavigationController
import ezlife.movil.oneparkingapp.util.Loader
import ezlife.movil.oneparkingapp.util.push
import ezlife.movil.oneparkingapp.util.subscribeWithError
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list_car.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class ListCarFragment : Fragment() {

    @Inject
    lateinit var viewModel: ListCarViewModel
    @Inject
    lateinit var loader: Loader
    @Inject
    lateinit var navigation: CarsNavigationController
    @Inject
    lateinit var adapter: CarAdapter
    lateinit var binding: FragmentListCarBinding

    val dis: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_car, container, false)
        binding.list.adapter = adapter
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        dis push viewModel.getCars()
                .subscribe { adapter.data = it }

        dis push btnAdd.clicks()
                .subscribe { navigation.navigateToAdd(false) }

        dis push adapter.onClearCar
                .flatMap { viewModel.deleteCar(it, adapter.data.size) }
                .subscribeWithError(
                        onNext = { toast(R.string.my_car_remove) },
                        onError = { toast(it.message!!) },
                        onHttpError = this::toast
                )

        dis push adapter.onSelectCar
                .flatMap(viewModel::selectCar)
                .subscribe()

        dis push swipe.refreshes()
                .flatMap { viewModel.reloadCars() }
                .subscribeWithError(
                        onNext = { swipe.isRefreshing = false },
                        onHttpError = this::toast
                )
    }

    override fun onStop() {
        super.onStop()
        dis.clear()
    }

    companion object {
        fun instance(): ListCarFragment = ListCarFragment()
    }

}
