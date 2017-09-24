package ezlife.movil.oneparkingapp.data.api

import ezlife.movil.oneparkingapp.data.api.model.InsertCarRes
import ezlife.movil.oneparkingapp.data.api.model.SimpleResponse
import ezlife.movil.oneparkingapp.data.db.model.Car
import io.reactivex.Observable
import retrofit2.http.*


interface CarApi{
    @POST("clientes/vehiculos")
    fun insertCar(@Header("Authorization") auth: String, @Body car: Car): Observable<InsertCarRes>

    @GET("clientes/{id}/vehiculos")
    fun getCars(@Header("Authorization") auth: String, @Path("id") id: String): Observable<MutableList<Car>>

    @DELETE("clientes/vehiculos/{plate}")
    fun deleteCar(@Header("Authorization") auth: String, @Path("plate") plate: String): Observable<SimpleResponse>
}