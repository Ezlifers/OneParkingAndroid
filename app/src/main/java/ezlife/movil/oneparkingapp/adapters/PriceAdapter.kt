package ezlife.movil.oneparkingapp.adapters

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.TemplatePriceBinding
import java.text.NumberFormat

class PriceAdapter(val timeMin: Int, val prices: List<Int>) : RecyclerView.Adapter<PriceHolder>() {

    val format: NumberFormat by lazy { NumberFormat.getCurrencyInstance() }

    init {
        format.maximumFractionDigits = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceHolder
            = PriceHolder(parent.inflate(R.layout.template_price))

    override fun onBindViewHolder(holder: PriceHolder, position: Int) {
        holder.binding.position = position
        holder.binding.price = format.format(prices[position])
        holder.binding.step = timeMin
    }

    override fun getItemCount(): Int = prices.size


    companion object {
        @JvmStatic
        @BindingAdapter("app:posTime", "app:stepTime")
        fun setTimePrice(view: TextView, posTime: Int, stepTime: Int) {
            val from = (posTime * stepTime) + 1
            val to = (posTime + 1) * stepTime
            view.text = "$from a $to"
        }
    }
}

class PriceHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: TemplatePriceBinding = DataBindingUtil.bind(view)
}

