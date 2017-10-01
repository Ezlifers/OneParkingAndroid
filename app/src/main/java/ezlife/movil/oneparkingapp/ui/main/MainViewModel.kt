package ezlife.movil.oneparkingapp.ui.main

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import ezlife.movil.oneparkingapp.data.api.CashApi
import ezlife.movil.oneparkingapp.data.api.SetupApi
import ezlife.movil.oneparkingapp.data.api.ZoneStateApi
import ezlife.movil.oneparkingapp.data.api.model.Setup
import ezlife.movil.oneparkingapp.data.api.model.ZoneState
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(private val carDao: CarDao,
                                        private val cashApi: CashApi,
                                        private val setupApi: SetupApi,
                                        private val zoneStateApi: ZoneStateApi,
                                        private val session: UserSession) : ViewModel() {

    val centerPosition: LatLng by lazy { LatLng(CENTER_LAT, CENTER_LNG) }
    private var lastPoint: LatLng? = null
    val day: Int by lazy {
        val dayWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
        when (dayWeek) {
            in Calendar.MONDAY..Calendar.FRIDAY -> 0
            Calendar.SATURDAY -> 1
            Calendar.SUNDAY -> 2
            else -> 0
        }
    }


    fun isDisability():Boolean = session.userDisability

    fun selectedCar(): Flowable<Car> = carDao.selected()
            .applyShedures()

    fun getCash(): Observable<Long> = Observable.concat(listOf(
            Observable.just(session.userCash),
            cashApi.getCash(session.makeToken(), session.userID)
                    .map {
                        session.userCash = it.saldo
                        it.saldo
                    }))
            .applyShedures()

    fun verifyCurrentSetup(): Observable<Int> = verifyCurrentDay()
            .flatMap { setupApi.getSetup(session.makeToken(), true) }
            .flatMap(this::verifyCurrentVersion)
            .applyShedures()

    private fun verifyCurrentDay(): Observable<Unit> = Observable.create {
        val day = session.setupDay
        val calendar = Calendar.getInstance()
        val currentDay = calendar[Calendar.DAY_OF_MONTH]
        if (day != currentDay) it.onNext(Unit)
        it.onComplete()
    }

    private fun verifyCurrentVersion(setup: Setup): Observable<Int> = Observable.create {
        val version = session.setupVersion
        if (version != setup.version) it.onNext(version)
        else {
            val calendar = Calendar.getInstance()
            session.setupDay = calendar[Calendar.DAY_OF_MONTH]
        }
        it.onComplete()
    }


    fun currentState(point: LatLng): Observable<List<ZoneState>> = Observable.create<List<LatLng?>> {
        if (lastPoint == null) {
            lastPoint = point
            it.onNext(listOf(point, null))
        } else {
            val meters = SphericalUtil.computeDistanceBetween(lastPoint, point)
            if (meters >= MIN_METERS) {
                it.onNext(listOf(point, lastPoint))
                lastPoint = point
            }
        }
        it.onComplete()
    }.flatMap {
        val calendar = Calendar.getInstance()
        val time = (calendar[Calendar.HOUR_OF_DAY] * 60) + calendar[Calendar.MINUTE]
        zoneStateApi.getStates(session.makeToken(), day, time,
                it[0]!!.latitude, it[0]!!.longitude,
                it[1]?.latitude, it[1]?.longitude)
    }.applyShedures()


    companion object {
        private val CENTER_LAT = 2.4419053
        private val CENTER_LNG = -76.6063383
        private val MIN_METERS = 250
    }

}