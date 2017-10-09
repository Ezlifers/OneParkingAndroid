package ezlife.movil.oneparkingapp.ui.main.setup

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.util.push
import ezlife.movil.oneparkingapp.util.setupArgs
import ezlife.movil.oneparkingapp.util.subscribeWithError
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class SetupFragment : DialogFragment() {

    @Inject
    lateinit var viewModel: SetupViewModel
    val dis: CompositeDisposable = CompositeDisposable()
    val version: Int by lazy { arguments.getInt(EXTRA_VERSION, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_setup, container, false)

    override fun onResume() {
        super.onResume()
        isCancelable = false
        dis push viewModel.setupApp(version)
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

    override fun onStop() {
        super.onStop()
        dis.dispose()
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