package ezlife.movil.oneparkingapp.providers

import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.util.SessionApp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.*

interface StateService{

    @GET("zonas/estados")
    fun getStates(@Header("Authorization") auth: String, @Query("day") day:Int
                  , @Query("timeHour") time:Int
                  , @Query("lat") lat:Double
                  , @Query("lon") lon:Double
                  , @Query("prevLat") prevLat:Double?
                  , @Query("prevLon") prevLon:Double?):Call<List<ZoneState>>
}

class StateProvider(val activity:AppCompatActivity){

    private val service: StateService = RetrofitHelper.retrofit.create(StateService::class.java)
    fun getStates(day:Int, timeHour:Int, lat:Double, lon:Double, prevLat:Double?, prevLon:Double?, callback: (states: List<ZoneState>) -> Unit){
        val req = service.getStates(SessionApp.makeToken(), day, timeHour, lat, lon, prevLat, prevLon)
        req.enqueue(ProviderCallback(activity) { res ->
            callback(res)
        })
    }
}

data class CurrentState(val libre: Date, val bahias: Int, var bahiasOcupadas: Int, val dis: Int, var disOcupadas: Int)
data class ZoneState(val _id:String, val tipo:Int, val localizacion: Point, val estado:CurrentState)