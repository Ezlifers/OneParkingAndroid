package ezlife.movil.oneparkingapp.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.TemplateCarBinding
import ezlife.movil.oneparkingapp.db.Car

class CarAdapter(private val setup: Boolean, var data:MutableList<Car>, private val removedCar:((position:Int)->Unit)? = null ,private val selectedCallback: ((position:Int) -> Unit)? = null) : RecyclerView.Adapter<CarHolder>() {

    private var prev: Int? = null

    override fun onBindViewHolder(holder: CarHolder, position: Int) {
        holder.binding.setup = setup
        holder.binding.car = data[position]
        holder.binding.handler = this
        holder.binding.root.tag = position
        holder.binding.clear.tag = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder = CarHolder(parent.inflate(R.layout.template_car))

    override fun getItemCount(): Int = data.size

    fun selectCar(position: Int) {
        if (prev == null) {
            prev = data.indexOfFirst { it.selected!! }
        }
        data[position].selected = true
        data[prev!!].selected = false
        prev = position
        notifyDataSetChanged()
        selectedCallback?.invoke(position)
    }

    fun removeCar(position: Int) {
        removedCar?.invoke(position)
    }

}

class CarHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: TemplateCarBinding = DataBindingUtil.bind(view)
}