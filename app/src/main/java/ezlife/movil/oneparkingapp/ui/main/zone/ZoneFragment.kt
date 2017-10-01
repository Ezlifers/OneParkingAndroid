package ezlife.movil.oneparkingapp.ui.main.zone

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.model.CurrentState
import ezlife.movil.oneparkingapp.data.api.model.ZoneState
import ezlife.movil.oneparkingapp.data.observer.MarkObserver
import ezlife.movil.oneparkingapp.data.observer.MarkerState
import ezlife.movil.oneparkingapp.databinding.FragmentZoneBinding
import ezlife.movil.oneparkingapp.fragments.setupArgs
import ezlife.movil.oneparkingapp.ui.main.MainNavigationController
import ezlife.movil.oneparkingapp.util.push
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_zone.*
import javax.inject.Inject

//TODO: Cuando hay un cambio de tipo Free -> trarification no se ocultan bn las partes
class ZoneFragment : DialogFragment() {

    @Inject
    lateinit var viewModel: ZoneViewModel
    @Inject
    lateinit var navigation: MainNavigationController
    @Inject
    lateinit var markObserver: MarkObserver

    lateinit var binding: FragmentZoneBinding
    private val state: ZoneViewModel.State by lazy { viewModel.getState(arguments) }
    private val dis: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zone, container, false)
        binding.state = state
        binding.type = state.type
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        dis push btnInfo.clicks()
                .subscribe { navigation.navigateToInfo(state.id, binding.zone.nombre) }

        dis push btnReport.clicks()
                .subscribe {
                    dismiss()
                    navigation.showDialogReport(state.id, binding.zone.codigo, binding.zone.nombre)
                }

        dis push btnDisability.clicks()
                .flatMap { validateBays(state.busyDis, state.dis) }
                .subscribe {
                    dismiss()
                    navigation.showDialogReserve(state, true)
                }

        dis push btnReserve.clicks()
                .flatMap { validateBays(state.busyBays, state.bays) }
                .subscribe {
                    dismiss()
                    navigation.showDialogReserve(state, false)
                }

        dis push viewModel.getZone(state.id)
                .subscribe { binding.zone = it }

        dis push viewModel.getTypeAndSateTo(state.id, state.day)
                .subscribe {
                    boardState.setBackgroundResource(it["color"] as Int)
                    binding.stateTo = it["message"] as String
                    val currentState = it["state"] as Int
                    if (state.type != currentState) {
                        binding.type = currentState
                        markObserver.markerState.onNext(MarkerState(state.id, currentState))
                    }
                }

        dis push markObserver.makerBusy
                .subscribe { updateState(it.type, it.dis) }


    }

    override fun onStop() {
        super.onStop()
        dis.dispose()
    }

    private fun updateState(type: Int, dis: Boolean) = when (type) {
        RESERVE_START -> {
            if (dis) state.busyDis += 1 else state.busyBays += 1
            binding.state = state
        }
        RESERVE_END -> {
            if (dis) state.busyDis -= 1 else state.busyBays -= 1
            binding.state = state
        }
        else -> {
        }
    }

    private fun validateBays(taken: Int, max: Int): Observable<Unit> = Observable.create {
        if (taken < max) it.onNext(Unit)
        it.onComplete()
    }

    //region Statics , Consts
    companion object {

        private val RESERVE_END = 1
        private val RESERVE_START = 0

        val KEY_ID = "id"
        val KEY_TYPE = "type"
        val KEY_DIS = "dis"
        val KEY_BUSY_DIS = "busyDis"
        val KEY_BAYS = "bays"
        val KEY_BUSY_BAYS = "busyBays"
        val KEY_FREE = "free"
        val KEY_DAY = "day"


        fun instance(state: ZoneState, day: Int): ZoneFragment {
            val fragment = ZoneFragment()
            val bayState: CurrentState? = state.estado
            fragment.setupArgs(KEY_ID to state._id,
                    KEY_TYPE to state.tipo,
                    KEY_BAYS to (bayState?.bahias ?: 0),
                    KEY_BUSY_BAYS to (bayState?.bahiasOcupadas ?: 0),
                    KEY_DIS to (bayState?.dis ?: 0),
                    KEY_BUSY_DIS to (bayState?.disOcupadas ?: 0),
                    KEY_FREE to (bayState?.libre?.time ?: 0),
                    KEY_DAY to day)
            return fragment
        }

        @JvmStatic
        @BindingAdapter("app:bays", "app:baysSize")
        fun setAvailable(view: TextView, bays: Int, baysSize: Int) {
            view.text = view.context.getString(R.string.bay_available, bays, baysSize)
            val color = if (bays < baysSize) R.color.active else R.color.notAvailable
            view.setTextColor(ContextCompat.getColor(view.context, color))
        }
    }


    //endregion
}

