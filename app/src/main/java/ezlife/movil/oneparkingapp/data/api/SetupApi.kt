package ezlife.movil.oneparkingapp.data.api

import ezlife.movil.oneparkingapp.data.api.model.Setup
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SetupApi {

    @GET("clientes/configuracion")
    fun getSetup(@Header("Authorization") auth: String, @Query("onlyVersion") onlyVersion: Boolean,
                 @Query("version") version: Int? = null): Observable<Setup>

}