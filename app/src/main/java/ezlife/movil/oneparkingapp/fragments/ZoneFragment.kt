package ezlife.movil.oneparkingapp.fragments


import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.providers.ZoneState
import java.util.*

class ZoneFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_zone, container, false)
    }

    fun reserve(dis: Boolean) {

    }

    fun report() {

    }

    fun goToInfo() {

    }

    //region Statics & Consts
    companion object {

        private val KEY_ID = "id"
        private val KEY_TYPE = "type"
        private val KEY_DIS = "dis"
        private val KEY_BUSY_DIS = "busyDis"
        private val KEY_BAYS = "bays"
        private val KEY_BUSY_BAYS = "busyBays"
        private val KEY_FREE = "free"


        fun instance(state: ZoneState): ZoneFragment {
            val fragment = ZoneFragment()
            val bayState = state.estado
            fragment.setupArgs(KEY_ID to state._id
                    , KEY_TYPE to state.tipo
                    , KEY_BAYS to bayState.bahias
                    , KEY_BUSY_BAYS to bayState.bahiasOcupadas
                    , KEY_DIS to bayState.dis
                    , KEY_BUSY_DIS to bayState.disOcupadas
                    , KEY_FREE to bayState.libre.time
            )
            return fragment
        }

        fun getState(args: Bundle): State {
            val id = args.getString(KEY_ID)
            val type = args.getString(KEY_TYPE)
            val bays = args.getInt(KEY_BAYS)
            val busyBays = args.getInt(KEY_BUSY_BAYS)
            val dis = args.getInt(KEY_DIS)
            val busyDis = args.getInt(KEY_BUSY_DIS)
            val freeMilis = args.getLong(KEY_FREE)
            val free = Date(freeMilis)

            val state = State(id, type, bays, busyBays, dis, busyDis, free)
            return state
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

data class State(val id: String
                 , val type: String
                 , val bays: Int
                 , var busyBays: Int
                 , val dis: Int
                 , var busyDis: Int
                 , var free: Date)
