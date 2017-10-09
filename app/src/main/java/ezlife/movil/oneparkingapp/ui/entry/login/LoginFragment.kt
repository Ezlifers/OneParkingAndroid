package ezlife.movil.oneparkingapp.ui.entry.login


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.model.User
import ezlife.movil.oneparkingapp.databinding.FragmentLoginBinding
import ezlife.movil.oneparkingapp.di.Injectable
import ezlife.movil.oneparkingapp.ui.entry.EntryNavigationController
import ezlife.movil.oneparkingapp.util.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModel: LoginViewModel
    @Inject
    lateinit var navigation: EntryNavigationController
    @Inject
    lateinit var loader: Loader
    lateinit var binding: FragmentLoginBinding

    val dis: LifeDisposable = LifeDisposable(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        binding.city = "Popayán"
        binding.loader = loader
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        dis add btnReg.clicks()
                .subscribe { navigation.navigateToRegister() }

        dis add btnIn.clicks()
                .flatMap { validateForm(R.string.login_form, username.text(), password.text()) }
                .loader(loader)
                .flatMap { viewModel.login(it[0], it[1]) }
                .subscribeWithError(
                        onNext = this::loginSuccess,
                        onError = this::loginFail,
                        onHttpError = this::toast
                )
    }

    private fun loginSuccess(user: User) {
        if (user.validado && user.vehiculos.isNotEmpty()) navigation.navigateToMain()
        else if (user.validado) navigation.navigateToAddCar()
        else navigation.navigateToPass(user.vehiculos.isNotEmpty())
    }

    private fun loginFail(error: Throwable) = when (error) {
        is HttpException -> toast(R.string.login_error)
        else -> toast(error.message!!)
    }

    companion object {
        fun instance(): LoginFragment = LoginFragment()
    }

}
