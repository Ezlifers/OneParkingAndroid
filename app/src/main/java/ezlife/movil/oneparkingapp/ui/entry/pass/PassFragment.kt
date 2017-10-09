package ezlife.movil.oneparkingapp.ui.entry.pass


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.FragmentPassBinding
import ezlife.movil.oneparkingapp.ui.entry.EntryNavigationController
import ezlife.movil.oneparkingapp.ui.entry.register.RegisterFragment
import ezlife.movil.oneparkingapp.util.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_pass.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class PassFragment : Fragment() {

    @Inject
    lateinit var viewModel: PassViewModel
    @Inject
    lateinit var navigation: EntryNavigationController
    @Inject
    lateinit var loader:Loader
    lateinit var binding:FragmentPassBinding

    private val hasCars: Boolean by lazy { arguments.getBoolean(EXTRA_CARS, false) }
    private val dis: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pass, container, false)
        binding.loader = loader
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        dis push btnOk.clicks()
                .flatMap { validateForm(R.string.pass_form, pass.text(), pass1.text()) }
                .flatMap { form -> viewModel.validatePasswords(form[0], form[1]).map { form[0] } }
                .loader(loader)
                .flatMap { viewModel.updatePass(it) }
                .subscribeWithError(
                        onNext = {
                            toast(R.string.pass_success)
                            if (hasCars) navigation.navigateToMain()
                            else navigation.navigateToAddCar()
                        },
                        onError = { toast(it.message!!) },
                        onHttpError = this::toast

                )
    }

    override fun onStop() {
        super.onStop()
        dis.clear()
    }

    companion object {

        private val EXTRA_CARS = "extraCars"

        fun instance(hasCars: Boolean): RegisterFragment {
            val fragment = RegisterFragment()
            fragment.setupArgs(EXTRA_CARS to hasCars)
            return fragment
        }
    }

}
