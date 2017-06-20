package ezlife.movil.oneparkingapp.providers

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.preferences
import ezlife.movil.oneparkingapp.activities.toast
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.db.Schedule
import ezlife.movil.oneparkingapp.db.Zone
import ezlife.movil.oneparkingapp.util.Preference
import ezlife.movil.oneparkingapp.util.SessionApp
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await
import ezlife.movil.oneparkingauxiliar.db.Config
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.*


data class Setup(val success: Boolean, val version: Int = 0, val config: Config?, val zones: List<ZoneBase>?)

data class Time(val d: Boolean, val ti: Int, val tf: Int)
data class TimeRange(val horarios: List<Time>)
data class Point(val coordinates: List<Double>)
data class ZoneBase(val _id: String
                    , val codigo: Int
                    , val nombre: String
                    , val direccion: String
                    , val localización: Point
                    , val tiempos: List<TimeRange>)

interface SetupService {
    @GET("clientes/configuracion")
    fun getSetup(@Header("Authorization") auth: String, @Query("onlyVersion") onlyVersion: Boolean, @Query("version") version: Int?): Call<Setup>
}

class SetupProvider(val activity: AppCompatActivity, var loading: ProgressDialog? = null) {
    private val service: SetupService = RetrofitHelper.retrofit.create(SetupService::class.java)

    fun getSetup(onlyVersion: Boolean, version: Int?, callback: (setup: Setup) -> Unit) {
        loading?.show()
        val req = service.getSetup(SessionApp.makeToken(), onlyVersion, version)
        req.enqueue(ProviderCallback(activity, loading) { setup ->
            if (setup.success) {
                callback(setup)
            } else {
                activity.toast(R.string.setup_error)
            }

        })
    }
}

fun setupApp(activity: AppCompatActivity, callback: (() -> Unit)? = null) {

    val preferences = activity.preferences()
    val savedDay = preferences.getInt(Preference.SETUP_DAY, -1)
    val savedVersion = preferences.getInt(Preference.SETUP_VERSION, -1)

    val calendar = Calendar.getInstance()
    val day = calendar[Calendar.DAY_OF_MONTH]

    if (day != savedDay) {
        val setupProvider = SetupProvider(activity)
        setupProvider.getSetup(true, null) { (_, currentVersion) ->
            if (savedVersion < currentVersion) {

                val progress: ProgressDialog = ProgressDialog(activity)
                progress.setMessage(activity.getString(R.string.setup_loading))
                progress.setCancelable(false)
                setupProvider.loading = progress

                setupProvider.getSetup(false, savedVersion) { (_, version, config, zones) ->
                    asyncUI {
                        await {
                            val configDao = DB.con.configDao()
                            config!!.preciosText = config.precio.joinToString(",")
                            configDao.delete()
                            configDao.insert(config)

                            val zoneDao = DB.con.zoneDao()
                            val scheduleDao = DB.con.scheduleDao()

                            val zonesDb: MutableList<Zone> = mutableListOf()
                            val schedulesDb: MutableList<Schedule> = mutableListOf()
                            val ids: MutableList<String> = mutableListOf()

                            for (zone in zones!!) {
                                ids.add(zone._id)
                                zonesDb.add(Zone(zone))
                                val timeRanges = zone.tiempos
                                timeRanges
                                        .map { it.horarios }
                                        .forEachIndexed { index, schedules -> schedules.mapTo(schedulesDb) { Schedule(zone._id, index, it) } }
                            }
                            zoneDao.delete(ids)
                            zoneDao.insert(zonesDb)
                            scheduleDao.deleteByZone(ids)
                            scheduleDao.insert(schedulesDb)
                        }
                        val editor =  preferences.edit()
                        editor.putInt(Preference.SETUP_VERSION, version)
                        editor.apply()
                        callback?.invoke()
                    }

                }

            } else {
                callback?.invoke()
            }
        }
    } else {
        callback?.invoke()
    }

}