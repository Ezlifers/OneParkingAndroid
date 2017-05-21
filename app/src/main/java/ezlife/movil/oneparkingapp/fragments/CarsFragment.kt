package ezlife.movil.oneparkingapp.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.MyCarsActivity
import ezlife.movil.oneparkingapp.adapters.CarAdapter
import ezlife.movil.oneparkingapp.databinding.FragmentCarsBinding
import ezlife.movil.oneparkingapp.db.Car
import ezlife.movil.oneparkingapp.db.CarDao
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.providers.CarProvider
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await


class CarsFragment : DialogFragment() {

    lateinit var binding: FragmentCarsBinding
    val provider: CarProvider by lazy { CarProvider(activity as AppCompatActivity, null) }
    val dao: CarDao by lazy { DB.con.carDao() }
    val adapter: CarAdapter = CarAdapter(false, mutableListOf(), selectedCallback = this::selectedCar)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cars, container, false)
        binding.handler = this
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            provider.getCars { reloadCars(it) }
        }
        binding.list.adapter = adapter
        loadCars()
        return binding.root
    }

    fun loadCars() = asyncUI {
        adapter.data = await { dao.all() }
        adapter.notifyDataSetChanged()
    }

    fun selectedCar(position: Int) = asyncUI {
        await { dao.update(adapter.data[position]) }
        dismiss()
    }

    fun reloadCars(cars: MutableList<Car>) = asyncUI {
        await {
            dao.deleteAll()
            dao.insertList(cars)
        }
        adapter.data = cars
        adapter.notifyDataSetChanged()
    }

    fun goToMyCars() {
        startActivity<MyCarsActivity>()
        dismiss()
    }

    companion object{
        fun instance() = CarsFragment()
    }

}
