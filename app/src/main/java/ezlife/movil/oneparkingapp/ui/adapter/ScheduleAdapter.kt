package ezlife.movil.oneparkingapp.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.db.model.Schedule
import ezlife.movil.oneparkingapp.databinding.TemplateDayBinding
import ezlife.movil.oneparkingapp.databinding.TemplateScheduleBinding
import ezlife.movil.oneparkingapp.util.inflate

class ScheduleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Any> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        0 -> DayHolder(parent.inflate(R.layout.template_day))
        else -> ScheduleHolder(parent.inflate(R.layout.template_schedule))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is DayHolder -> holder.binding.day = data[position] as Int
        is ScheduleHolder -> holder.binding.time = data[position] as Schedule
        else -> {
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is Int -> 0
        else -> 1
    }


}

class DayHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: TemplateDayBinding = DataBindingUtil.bind(view)
}

class ScheduleHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: TemplateScheduleBinding = DataBindingUtil.bind(view)
}