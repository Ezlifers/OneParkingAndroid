package ezlife.movil.oneparkingapp.ui.infozone.schedule

import android.arch.lifecycle.ViewModel
import android.util.Log
import ezlife.movil.oneparkingapp.data.db.dao.ScheduleDao
import ezlife.movil.oneparkingapp.data.db.model.Schedule
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.rxkotlin.toObservable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(private val dao: ScheduleDao) : ViewModel() {

    fun getSchedule(id: String): Single<List<Any>> = dao.all(id)
            .toObservable()
            .flatMap { it.toObservable() }
            .map { it as Any }
            .groupBy { (it as Schedule).dia }
            .flatMap { gp -> gp.startWith(gp.key) }
            .toList()
            .applyShedures()

}