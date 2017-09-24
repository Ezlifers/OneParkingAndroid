package ezlife.movil.oneparkingapp.ui.cars.list

import android.arch.lifecycle.ViewModel
import android.content.Context
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.CarApi
import ezlife.movil.oneparkingapp.data.api.model.SimpleResponse
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class ListCarViewModel @Inject constructor(private val dao: CarDao,
                                           private val api: CarApi,
                                           private val session: UserSession,
                                           private val context: Context) : ViewModel() {


    fun getCars(): Flowable<List<Car>> = dao.all()
            .applyShedures()

    fun selectCar(car: Car): Observable<Unit> = Observable.fromCallable { dao.selected() }
            .flatMap {
                it.selected = false
                Observable.fromCallable { dao.update(it) }
            }
            .flatMap {
                car.selected = true
                Observable.fromCallable { dao.update(car) }
            }


    fun reloadCars(): Observable<Unit> = api.getCars(session.makeToken(), session.userID)
            .flatMap { cars -> Observable.fromCallable { dao.deleteAll() }.map { cars } }
            .flatMap {
                Observable.fromCallable {
                    it.sortBy { it.apodo }
                    if (it.size > 0) it[0].selected = true
                    dao.insertList(it)
                }
            }.applyShedures()


    fun deleteCar(car: Car, listSize: Int) = validateMinSize(car, listSize)
            .flatMap { api.deleteCar(session.makeToken(), it.placa) }
            .flatMap { validateDeleteSuccess(it, car) }
            .flatMap(this::deleteLocalCar)
            .applyShedures()

    private fun validateMinSize(car: Car, size: Int): Observable<Car> = Observable.create {
        if (size > 1) it.onNext(car)
        else it.onError(Throwable(context.getString(R.string.my_car_remove_alert)))
        it.onComplete()
    }

    private fun validateDeleteSuccess(res: SimpleResponse, car: Car): Observable<Car> = Observable.create {
        if (res.success) it.onNext(car)
        else it.onError(Throwable(context.getString(R.string.my_car_remove_error)))
        it.onComplete()
    }

    private fun deleteLocalCar(car: Car): Observable<Unit> =
            if (car.selected == true) {
                Observable.fromCallable {
                    dao.delete(car)
                    dao.next()
                }.flatMap {
                    it.selected = true
                    Observable.fromCallable { dao.update(it) }
                }
            } else {
                Observable.fromCallable { dao.delete(car) }
            }


}