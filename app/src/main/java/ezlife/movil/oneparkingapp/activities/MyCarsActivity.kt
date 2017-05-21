package ezlife.movil.oneparkingapp.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.adapters.CarAdapter
import ezlife.movil.oneparkingapp.databinding.MyCarsBinding
import ezlife.movil.oneparkingapp.db.Car
import ezlife.movil.oneparkingapp.db.CarDao
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.providers.CarProvider
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await

class MyCarsActivity : AppCompatActivity() {

    lateinit var binding: MyCarsBinding
    val provider: CarProvider by lazy { CarProvider(this, null) }
    val dao: CarDao by lazy { DB.con.carDao() }
    val adapter: CarAdapter = CarAdapter(false, mutableListOf(), this::removeCar, this::selectedCar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cars)
        binding.handler = this
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            provider.getCars { reloadCars(it) }
        }
        binding.list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadCars()
    }

    fun loadCars() = asyncUI {
        adapter.data = await { dao.all() }
        adapter.notifyDataSetChanged()
    }

    fun reloadCars(cars: MutableList<Car>) = asyncUI {
        await {
            dao.deleteAll()
            dao.insertList(cars)
        }
        adapter.data = cars
        adapter.notifyDataSetChanged()
    }

    fun addCar(){
        startActivity<AddCarActivity>(AddCarActivity.EXTRA_FIRST_TIME to false)
    }

    fun selectedCar(position: Int) = asyncUI {
        await { dao.update(adapter.data[position]) }
    }

    fun removeCar(position:Int) = asyncUI{
        if(adapter.data.size > 0){
            await { dao.delete(adapter.data[position]) }
            adapter.data.removeAt(position)
            adapter.notifyDataSetChanged()
        }else{
            toast(R.string.my_car_remove_error)
        }
    }
}
