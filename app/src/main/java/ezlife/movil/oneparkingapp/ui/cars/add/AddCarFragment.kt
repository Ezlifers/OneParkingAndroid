package ezlife.movil.oneparkingapp.ui.cars.add


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.MapActivity
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.databinding.FragmentAddCarBinding
import ezlife.movil.oneparkingapp.fragments.setupArgs
import ezlife.movil.oneparkingapp.fragments.startActivity
import ezlife.movil.oneparkingapp.fragments.toast
import ezlife.movil.oneparkingapp.ui.cars.CarsNavigationController
import ezlife.movil.oneparkingapp.util.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_car.*
import javax.inject.Inject

class AddCarFragment : Fragment() {

    @Inject
    lateinit var viewModel: AddCarViewModel
    @Inject
    lateinit var loader: Loader
    @Inject
    lateinit var navigation: CarsNavigationController
    lateinit var binding: FragmentAddCarBinding
    private val firstTime: Boolean by lazy { arguments.getBoolean(EXTRA_FIRST_TIME, false) }

    val dis: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_car, container, false)
        binding.loader = loader
        binding.firstTime = firstTime
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dis push btnAdd.clicks()
                .flatMap { validateForm(R.string.report_form, plate.text(), nickname.text(), brand.text()) }
                .loader(loader)
                .flatMap { viewModel.insert(Car(it[0], it[1], it[2], firstTime)) }
                .subscribeWithError(
                        onNext = {
                            toast(R.string.car_insert)
                            if (firstTime) startActivity<MapActivity>()
                            navigation.goToBack(firstTime)
                        },
                        onError = { toast(it.message!!) },
                        onHttpError = this::toast
                )
    }

    override fun onStop() {
        super.onStop()
        dis.dispose()
    }

    companion object {
        private val EXTRA_FIRST_TIME = "extra_first_time"
        fun instance(firstTime: Boolean): AddCarFragment {
            val fragment = AddCarFragment()
            fragment.setupArgs(EXTRA_FIRST_TIME to firstTime)
            return fragment
        }
    }

}
