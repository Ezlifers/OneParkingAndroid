package ezlife.movil.oneparkingapp.ui.main.cars

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.adapters.CarAdapter
import ezlife.movil.oneparkingapp.databinding.FragmentCarsBinding
import ezlife.movil.oneparkingapp.db.CarDao
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await

class CarsFragment : DialogFragment() {

    lateinit var binding: FragmentCarsBinding
    val dao: CarDao by lazy { DB.con.carDao() }
    val adapter: CarAdapter = CarAdapter(false, mutableListOf(), selectedCallback = this::selectedCar)
    var listener: OnCarSelectListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as OnCarSelectListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cars, container, false)
        //binding.handler = this
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
        loadCars()
        return binding.root
    }

    fun loadCars() = asyncUI {
        //adapter.data = await { dao.all() }
        adapter.notifyDataSetChanged()
    }

    fun selectedCar(position: Int, prev:Int) = asyncUI {
        await {
            //dao.update(adapter.data[position])
            //dao.update(adapter.data[prev])
        }
        listener?.onCarSelected()
        dismiss()
    }

    fun goToMyCars() {
        //startActivity<MyCarsActivity>()
        dismiss()
    }

    companion object{
        fun instance() = CarsFragment()
    }

}

interface OnCarSelectListener{
    fun onCarSelected()
}