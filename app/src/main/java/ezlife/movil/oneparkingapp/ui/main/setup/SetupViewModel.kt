package ezlife.movil.oneparkingapp.ui.main.setup

import android.arch.lifecycle.ViewModel
import android.content.Context
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.SetupApi
import ezlife.movil.oneparkingapp.data.api.model.Setup
import ezlife.movil.oneparkingapp.data.db.dao.ConfigDao
import ezlife.movil.oneparkingapp.data.db.dao.ScheduleDao
import ezlife.movil.oneparkingapp.data.db.dao.ZoneDao
import ezlife.movil.oneparkingapp.data.db.model.Schedule
import ezlife.movil.oneparkingapp.data.db.model.Zone
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
//TODO: Algo pasa con las versiones, que esta generando algun error
class SetupViewModel @Inject constructor(private val context: Context,
                                         private val setupApi: SetupApi,
                                         private val configDao: ConfigDao,
                                         private val zoneDao: ZoneDao,
                                         private val scheduleDao: ScheduleDao,
                                         private val session: UserSession) : ViewModel() {

    fun setupApp(currentVersion: Int): Observable<Unit> = setupApi.getSetup(session.makeToken(), false, currentVersion)
            .flatMap(this::validateSetupSucess)
            .flatMap(this::insertConfig)
            .flatMap(this::insertZone)
            .flatMap {
                Observable.fromCallable {
                    session.setupVersion = it.version
                    val calendar = Calendar.getInstance()
                    session.setupDay = calendar[Calendar.DAY_OF_MONTH]
                }
            }.applyShedures()

    private fun validateSetupSucess(setup: Setup): Observable<Setup> = Observable.create {
        if (setup.success) it.onNext(setup)
        else Throwable(context.getString(R.string.setup_error))
        it.onComplete()
    }

    private fun insertConfig(setup: Setup): Observable<Setup> = Observable.fromCallable {
        configDao.delete()
        configDao.insert(setup.config!!)
        setup
    }

    private fun insertZone(setup: Setup): Observable<Setup> = Observable.fromCallable {
        val localZones: MutableList<Zone> = mutableListOf()
        val localSchedules: MutableList<Schedule> = mutableListOf()
        val ids: MutableList<String> = mutableListOf()

        for (zone in setup.zones!!) {
            ids.add(zone._id)
            localZones.add(Zone(zone))
            val timeRanges = zone.tiempos
            timeRanges
                    .map { it.horarios }
                    .forEachIndexed { index, schedules -> schedules.mapTo(localSchedules) { Schedule(zone._id, index, it) } }
        }

        zoneDao.delete(ids)
        zoneDao.insert(localZones)
        scheduleDao.deleteByZone(ids)
        scheduleDao.insert(localSchedules)
        setup
    }

}