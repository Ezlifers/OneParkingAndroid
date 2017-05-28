package ezlife.movil.oneparkingapp.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
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
    val adapter: CarAdapter = CarAdapter(true, mutableListOf(), this::removeCar, this::selectedCar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cars)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.handler = this
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            provider.getCars { reloadCars(it) }
        }
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
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
        if(cars.isNotEmpty()){
            cars[0].selected = true
        }
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

    fun selectedCar(position: Int, prev:Int) = asyncUI {
        await {
            dao.update(adapter.data[position])
            dao.update(adapter.data[prev])
        }
    }

    fun removeCar(position:Int){
        if(adapter.data.size > 1){
            binding.swipe.isRefreshing = true
            provider.deleteCar(adapter.data[position].placa){success ->
                if(success){
                    asyncUI{
                        val car = adapter.data.removeAt(position)
                        await {
                            if(car.selected != null && car.selected!!){
                                adapter.data[0].selected = true
                                dao.update(adapter.data[0])
                            }
                            dao.delete(car)
                        }
                        adapter.notifyDataSetChanged()
                        binding.swipe.isRefreshing = false
                        toast(R.string.my_car_remove)
                    }
                }else{
                    toast(R.string.my_car_remove_error)
                }
            }
        }else{
            toast(R.string.my_car_remove_alert)
        }
    }
}
