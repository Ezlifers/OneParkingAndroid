package ezlife.movil.oneparkingapp.ui.main.zone

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.model.ZoneState
import ezlife.movil.oneparkingapp.data.db.dao.ScheduleDao
import ezlife.movil.oneparkingapp.data.db.dao.ZoneDao
import ezlife.movil.oneparkingapp.data.db.model.Zone
import ezlife.movil.oneparkingapp.util.applyShedures
import ezlife.movil.oneparkingapp.util.toTimeFormat
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class ZoneViewModel @Inject constructor(private val context: Context,
                                        private val zoneDao: ZoneDao,
                                        private val schedulerDao: ScheduleDao) : ViewModel() {

    private val currentMin: Int by lazy {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] * 60 + calendar[Calendar.MINUTE]
    }

    fun getZone(id: String): Observable<Zone> =
            Observable.fromCallable { zoneDao.zoneById(id) }
                    .applyShedures()

    fun getTypeAndSateTo(id: String, day: Int): Observable<Map<String, *>> =
            Observable.fromCallable { schedulerDao.allByZone(id, day) }
                    .flatMap { times ->
                        Observable.create<Map<String, *>> {
                            var current = -1
                            var betweenTime = false
                            var step = -1
                            val currentState: Int

                            for ((index, time) in times.withIndex()) {
                                if (currentMin < time.ti) {
                                    betweenTime = true
                                    step = index
                                    break
                                } else if (currentMin in time.ti until time.tf) {
                                    current = index
                                    break
                                }
                                step = index
                            }

                            if (current > -1) {
                                val time = times[current]
                                currentState = if (time.d) ZoneState.TARIFICATION else ZoneState.PROHIBITED
                                it.onNext(mapOf("color" to if (time.d) R.color.colorPrimary else R.color.notAvailable,
                                        "message" to context.getString(R.string.zone_to, time.tf.toTimeFormat()),
                                        "state" to currentState))
                            } else if (betweenTime && step != times.size - 1) {
                                currentState = ZoneState.FREE
                                val timeNext = times[step + 1]
                                it.onNext(mapOf("color" to R.color.colorPrimary,
                                        "message" to context.getString(R.string.zone_to, timeNext.ti.toTimeFormat()),
                                        "state" to currentState))
                            } else {
                                currentState = ZoneState.FREE
                                it.onNext(mapOf("color" to R.color.colorPrimary,
                                        "message" to context.getString(R.string.zone_to_last),
                                        "state" to currentState))
                            }
                        }
                    }.applyShedures()

    fun getState(args: Bundle): State {
        val id = args.getString(ZoneFragment.KEY_ID)
        val type = args.getInt(ZoneFragment.KEY_TYPE)
        val bays = args.getInt(ZoneFragment.KEY_BAYS)
        val busyBays = args.getInt(ZoneFragment.KEY_BUSY_BAYS)
        val dis = args.getInt(ZoneFragment.KEY_DIS)
        val busyDis = args.getInt(ZoneFragment.KEY_BUSY_DIS)
        val freeMilis = args.getLong(ZoneFragment.KEY_FREE)
        val free = Date(freeMilis)
        val day = args.getInt(ZoneFragment.KEY_DAY)

        return ZoneViewModel.State(id, type, bays, busyBays, dis, busyDis, free, day)
    }

    data class State(val id: String,
                     val type: Int,
                     val bays: Int,
                     var busyBays: Int,
                     val dis: Int = 0,
                     var busyDis: Int,
                     var free: Date,
                     val day: Int)


}
