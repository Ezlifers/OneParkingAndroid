package ezlife.movil.oneparkingapp.fragments


import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.adapters.ScheduleAdapter
import ezlife.movil.oneparkingapp.databinding.FragmentScheduleBinding
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.db.ScheduleDao
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await
import ezlife.movil.oneparkingapp.util.toTimeFormat

class ScheduleFragment : Fragment() {
    lateinit var binding: FragmentScheduleBinding
    val dao: ScheduleDao = DB.con.scheduleDao()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        val zone = arguments.getString(ZONE)
        loadTimes(zone)
        return binding.root
    }

    private fun loadTimes(zone: String) = asyncUI {
        val times = await { dao.all(zone) }
        val data = mutableListOf<Any>()
        var day = -1
        for (t in times) {
            if (day != t.dia) {
                data.add(t.dia)
                day = t.dia
            }

            data.add(t)
        }
        binding.list.adapter = ScheduleAdapter(data)
        binding.list.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        val ZONE = "zone"
        fun instance(zone: String): ScheduleFragment {
            val fragment = ScheduleFragment()
            fragment.setupArgs(ZONE to zone)
            return fragment
        }

        @JvmStatic
        @BindingAdapter("app:scheduleFrom", "app:scheduleTo")
        fun setTime(txt: TextView, from: Int, to:Int) {
            txt.text = "${from.toTimeFormat()} - ${to.toTimeFormat()}"
        }
    }

}
