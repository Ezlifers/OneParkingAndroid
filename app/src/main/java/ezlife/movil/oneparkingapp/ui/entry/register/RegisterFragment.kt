package ezlife.movil.oneparkingapp.ui.entry.register

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.model.RegisterReq
import ezlife.movil.oneparkingapp.databinding.FragmentRegisterBinding
import ezlife.movil.oneparkingapp.fragments.toast
import ezlife.movil.oneparkingapp.ui.entry.EntryNavigationController
import ezlife.movil.oneparkingapp.util.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

class RegisterFragment : Fragment() {

    @Inject
    lateinit var viewModel: RegisterViewModel
    @Inject
    lateinit var navigation: EntryNavigationController
    @Inject
    lateinit var loader: Loader
    lateinit var binding: FragmentRegisterBinding
    val dis: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.secondScreen = false
        binding.loader = loader
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dis push btnBack.clicks()
                .subscribe { binding.secondScreen = false }

        dis push btnNext.clicks()
                .flatMap { validateForm(R.string.reg_form, name.text(), identity.text(), cel.text()) }
                .subscribe { binding.secondScreen = true }

        dis push btnReg.clicks()
                .flatMap { validateForm(R.string.reg_form, email.text(), pass.text(), pass2.text()) }
                .flatMap { viewModel.validatePasswords(it[1], it[2]) }
                .loader(loader)
                .flatMap {
                    viewModel.signIn(RegisterReq("Cliente", name.text(), identity.text(),
                            cel.text(), email.text(), email.text(), pass.text(), disability.isChecked))
                }
                .subscribeWithError(
                        onNext = { registerSuccess() },
                        onError = { toast(it.message!!) },
                        onHttpError = { toast(it) }
                )
    }

    private fun registerSuccess() {
        toast(R.string.reg_success)
        navigation.navigateToAddCar()
    }

    override fun onStop() {
        super.onStop()
        dis.clear()
    }

    companion object {
        fun instance(): RegisterFragment = RegisterFragment()
    }

}
