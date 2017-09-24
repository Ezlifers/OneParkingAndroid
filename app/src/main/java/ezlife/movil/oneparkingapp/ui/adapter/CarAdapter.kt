package ezlife.movil.oneparkingapp.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.databinding.TemplateCarBinding
import ezlife.movil.oneparkingapp.util.inflate
import io.reactivex.subjects.PublishSubject

class CarAdapter(private val setup: Boolean) : RecyclerView.Adapter<CarHolder>() {

    var data: List<Car> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    val onClearCar: PublishSubject<Car> = PublishSubject.create()
    val onSelectCar: PublishSubject<Car> = PublishSubject.create()


    override fun onBindViewHolder(holder: CarHolder, position: Int) {
        holder.binding.setup = setup
        holder.binding.car = data[position]
        holder.binding.onClearCar = onClearCar
        holder.binding.onSelectCar = onSelectCar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder
            = CarHolder(parent.inflate(R.layout.template_car))

    override fun getItemCount(): Int = data.size

}

class CarHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: TemplateCarBinding = DataBindingUtil.bind(view)
}