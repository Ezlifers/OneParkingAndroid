package ezlife.movil.oneparkingapp.ui.main.setup

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.di.Injectable
import ezlife.movil.oneparkingapp.util.LifeDisposable
import ezlife.movil.oneparkingapp.util.setupArgs
import ezlife.movil.oneparkingapp.util.subscribeWithError
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class SetupFragment : DialogFragment(), Injectable {

    @Inject
    lateinit var viewModel: SetupViewModel
    val dis: LifeDisposable = LifeDisposable(this)
    val version: Int by lazy { arguments.getInt(EXTRA_VERSION, 0) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_setup, container, false)

    override fun onResume() {
        super.onResume()
        isCancelable = false
        dis add viewModel.setupApp(version)
                .subscribeWithError(
                        onNext = { dismiss() },
                        onError = {
                            toast(it.message!!)
                            dismiss()
                        },
                        onHttpError = {
                            toast(it)
                            dismiss()
                        }
                )
    }

    companion object {
        private val EXTRA_VERSION = "extra_version"
        fun instance(version: Int): SetupFragment {
            val fragment = SetupFragment()
            fragment.setupArgs(EXTRA_VERSION to version)
            return fragment
        }
    }

}