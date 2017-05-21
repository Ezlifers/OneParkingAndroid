package ezlife.movil.oneparkingapp.providers

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.util.SessionApp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.*

interface ZoneService{

    @GET("zonas?settings=true&state=false&bays=false&defaults=false")
    fun getZones(@Header("Authorization") auth: String): Call<List<ZoneBase>>

    @GET("zonas/estados")
    fun getStates(@Header("Authorization") auth: String, @Query("day") day:Int
                  , @Query("timeHour") time:Int
                  , @Query("lat") lat:Double
                  , @Query("lon") lon:Double
                  , @Query("prevLat") prevLat:Double?
                  , @Query("prevLon") prevLon:Double?):Call<List<State>>
}

class ZoneProvider(val activity:AppCompatActivity, val loading:ProgressDialog? = null){

    private val service: ZoneService = RetrofitHelper.retrofit.create(ZoneService::class.java)
    fun getZones(callback:(zones:List<ZoneBase>)->Unit){
        loading?.show()
        val req = service.getZones(SessionApp.makeToken())
        req.enqueue(ProviderCallback(activity, loading) { res ->
            callback(res)
        })
    }

    fun getStates(day:Int, timeHour:Int, lat:Double, lon:Double, prevLat:Double?, prevLon:Double?, callback: (states: List<State>) -> Unit){
        val req = service.getStates(SessionApp.makeToken(), day, timeHour, lat, lon, prevLat, prevLon)
        req.enqueue(ProviderCallback(activity) { res ->
            callback(res)
        })
    }

}

data class Schedule(val d: Boolean, val ti: Int, val tf: Int, val dias: List<Int>, val p: Int)
data class TimeRange(val horarios: List<Schedule>)
data class Config(val tiempoMax: Int, val tiempoMin: Int, val tiempos: List<TimeRange>)
data class Point(val coordinates: List<Double>)
data class ZoneBase(val _id: String
                , val codigo: Int
                , val nombre: String
                , val direccion: String
                , val localizacion: Point
                , val configuracion: Config)

data class State(val libre: Date, val bahias: Int, var bahiasOcupadas: Int, val dis: Int, var disOcupadas: Int)