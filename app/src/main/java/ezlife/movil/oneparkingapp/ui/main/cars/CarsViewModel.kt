package ezlife.movil.oneparkingapp.ui.main.cars

import android.arch.lifecycle.ViewModel
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class CarsViewModel @Inject constructor(private val dao: CarDao) : ViewModel() {

    fun getCars(): Flowable<List<Car>> = dao.all()
            .applyShedures()

    fun selectCar(car: Car): Observable<Unit> = Observable.fromCallable { dao.onlySelected() }
            .flatMap {
                it.selected = false
                Observable.fromCallable { dao.update(it) }
            }
            .flatMap {
                car.selected = true
                Observable.fromCallable { dao.update(car) }
            }
            .applyShedures()

}