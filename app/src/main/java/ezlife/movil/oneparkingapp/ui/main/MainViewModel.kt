package ezlife.movil.oneparkingapp.ui.main

import android.arch.lifecycle.ViewModel
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Flowable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val dao: CarDao) : ViewModel() {

    fun selectedCar(): Flowable<Car> = dao.selected()
            .applyShedures()

}