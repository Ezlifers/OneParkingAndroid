package ezlife.movil.oneparkingapp.ui.cars.add

import android.arch.lifecycle.ViewModel
import android.content.Context
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.CarApi
import ezlife.movil.oneparkingapp.data.api.model.InsertCarRes
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Observable
import javax.inject.Inject

class AddCarViewModel @Inject constructor(private val dao: CarDao,
                                          private val api: CarApi,
                                          private val session: UserSession,
                                          private val context: Context) : ViewModel() {

    fun insert(car: Car): Observable<Unit> =
            api.insertCar(session.makeToken(), car)
                    .flatMap { validateSuccess(it, car) }
                    .flatMap { Observable.fromCallable { dao.insert(car) } }
                    .applyShedures()


    private fun validateSuccess(res: InsertCarRes, car: Car): Observable<Car> = Observable.create {
        if (res.success) {
            if (car.selected!!) session.logged = true
            it.onNext(car)
        } else Throwable(
                if (res.outRange) context.getString(R.string.car_insert_error_2)
                else context.getString(R.string.car_insert_error_2))
    }


}


