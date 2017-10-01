package ezlife.movil.oneparkingapp.data.api

import ezlife.movil.oneparkingapp.data.api.model.ZoneState
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ZoneStateApi {

    @GET("zonas/estados")
    fun getStates(@Header("Authorization") auth: String, @Query("day") day: Int
                  , @Query("timeHour") time: Int
                  , @Query("lat") lat: Double
                  , @Query("lon") lon: Double
                  , @Query("prevLat") prevLat: Double? = null
                  , @Query("prevLon") prevLon: Double? = null): Observable<List<ZoneState>>
}