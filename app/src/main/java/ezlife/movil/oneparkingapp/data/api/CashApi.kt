package ezlife.movil.oneparkingapp.data.api

import ezlife.movil.oneparkingapp.data.api.model.Cash
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CashApi {
    @GET("clientes/{id}/saldo")
    fun getCash(@Header("Authorization") auth: String, @Path("id") id: String): Observable<Cash>
}