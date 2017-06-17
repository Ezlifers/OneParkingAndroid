package ezlife.movil.oneparkingapp.fragments


import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.InfoZoneActivity
import ezlife.movil.oneparkingapp.databinding.FragmentZoneBinding
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.db.ScheduleDao
import ezlife.movil.oneparkingapp.db.ZoneDao
import ezlife.movil.oneparkingapp.providers.ZoneState
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await
import ezlife.movil.oneparkingapp.util.toTimeFormat
import java.util.*

class ZoneFragment : DialogFragment() {

    lateinit var binding: FragmentZoneBinding
    lateinit var listener: ZoneDialogListener
    val state: State by lazy { getState(arguments) }
    val zoneDao: ZoneDao = DB.con.zoneDao()
    val scheduleDao: ScheduleDao = DB.con.scheduleDao()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context) {
            is ZoneDialogListener -> listener = context
            else -> throw RuntimeException("${context.toString()} must implement ZoneDialogListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zone, container, false)
        binding.handler = this
        binding.state = state
        binding.stateType = state.type

        loadZone()
        return binding.root
    }

    private fun loadZone() = asyncUI {
        binding.zone = await { zoneDao.zoneById(state.id) }

        val calendar = Calendar.getInstance()
        val day = when(calendar[Calendar.DAY_OF_WEEK]){
            in 1..5 -> 0
            6 -> 1
            else -> 2
        }
        val min = calendar[Calendar.HOUR_OF_DAY]*60 + calendar[Calendar.MINUTE]
        val times = await { scheduleDao.allByZone(state.id, day) }

        var current = -1
        var betweenTime = false
        var step = -1
        val lastIndex = times.size - 1
        val currentState:Int

        for(i in 0 until times.size){
            val time = times[i]
            if(min < time.ti){
                betweenTime = true
                step = i
                break
            }else if(min in time.ti until time.tf){
                current = i
                break
            }
            step = i
        }

        if(current > -1){
            val time =  times[current]
            currentState = if(time.d) TYPE_TARIFICATION else TYPE_PROHIBITED
            updateStateBoard(currentState, time.tf)
        }else if(betweenTime && step != lastIndex){
            currentState = TYPE_FREE
            val timeNext =  times[step + 1]
            updateStateBoard(currentState, timeNext.ti)
        }else{
            currentState = TYPE_FREE
            updateStateBoard(currentState, -1)
        }

        if(state.type !=  currentState){
            binding.stateType = currentState
            listener.updateMark(state.id, currentState)
        }

    }

    fun updateStateBoard(type:Int, timeEnd:Int)= when(type){
        TYPE_PROHIBITED->{
            binding.boardState.setBackgroundResource(R.color.notAvailable)
            binding.stateTo = getString(R.string.zone_to, timeEnd.toTimeFormat())
        }
        TYPE_TARIFICATION->{
            binding.boardState.setBackgroundResource(R.color.colorPrimary)
            binding.stateTo = getString(R.string.zone_to, timeEnd.toTimeFormat())
        }
        else->{
            binding.boardState.setBackgroundResource(R.color.colorPrimary)
            binding.stateTo = if(timeEnd > 0) getString(R.string.zone_to, timeEnd.toTimeFormat()) else getString(R.string.zone_to_last)
        }
    }

    fun reserve(dis: Boolean) {
        if (dis && state.busyDis < state.dis) {
            listener.onReserveDialog(state, true)
        } else if (state.busyBays < state.bays) {
            listener.onReserveDialog(state, false)
        } else {
            toast(R.string.reserve_busy)
        }
    }

    fun report() {
        listener.onReportDialog(state)
    }

    fun goToInfo() {
        startActivity<InfoZoneActivity>(InfoZoneActivity.EXTRA_ID to state.id)
    }

    fun updateState(type: Int, dis: Boolean) = when (type) {
        RESERVE_START -> {
            if(dis) state.busyDis += 1 else state.busyBays += 1
            binding.state = state
        }
        RESERVE_END -> {
            if(dis) state.busyDis -= 1 else state.busyBays -= 1
            binding.state = state
        }
        else -> {
        }
    }

    //region Statics & Consts
    companion object {

        private val RESERVE_END = 1
        private val RESERVE_START = 0

        private val TYPE_FREE = 0
        private val TYPE_TARIFICATION = 1
        private val TYPE_PROHIBITED = 2

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
            val type = args.getInt(KEY_TYPE)
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

interface ZoneDialogListener {
    fun onReserveDialog(state: State, disability: Boolean)
    fun onReportDialog(state: State)
    fun updateMark(idZone: String, state: Int)
}

data class State(val id: String
                 , val type: Int
                 , val bays: Int
                 , var busyBays: Int
                 , val dis: Int = 0
                 , var busyDis: Int
                 , var free: Date)
