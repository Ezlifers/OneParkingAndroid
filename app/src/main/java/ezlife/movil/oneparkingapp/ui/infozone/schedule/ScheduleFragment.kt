package ezlife.movil.oneparkingapp.ui.infozone.schedule

import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dagger.android.support.AndroidSupportInjection
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.ui.adapter.ScheduleAdapter
import ezlife.movil.oneparkingapp.util.LifeDisposable
import ezlife.movil.oneparkingapp.util.push
import ezlife.movil.oneparkingapp.util.setupArgs
import ezlife.movil.oneparkingapp.util.toTimeFormat
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_schedule.*
import javax.inject.Inject

class ScheduleFragment : Fragment() {

    @Inject
    lateinit var viewModel: ScheduleViewModel
    val adapter: ScheduleAdapter = ScheduleAdapter()
    val dis: LifeDisposable = LifeDisposable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_schedule, container, false)

    override fun onResume() {
        super.onResume()
        list.adapter = adapter
        val id = arguments.getString(ZONE)
        dis add viewModel.getSchedule(id)
                .subscribe { data ->
                    adapter.data = data
                }
    }

    //region Instance & BindinsAdapter
    companion object {
        val ZONE = "zone"
        fun instance(zone: String): ScheduleFragment {
            val fragment = ScheduleFragment()
            fragment.setupArgs(ZONE to zone)
            return fragment
        }

        @JvmStatic
        @BindingAdapter("app:scheduleFrom", "app:scheduleTo")
        fun setTime(txt: TextView, from: Int, to: Int) {
            txt.text = "${from.toTimeFormat()} - ${to.toTimeFormat()}"
        }
    }
    //endregion

}