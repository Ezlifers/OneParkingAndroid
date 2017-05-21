package ezlife.movil.oneparkingapp.providers

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.db.Car
import ezlife.movil.oneparkingapp.util.SessionApp
import retrofit2.Call
import retrofit2.http.*

data class InsertCarRes(val success:Boolean, val outRange:Boolean)

interface CarService{
    @POST("clientes/vehiculos")
    fun insertCar(@Header("Authorization") auth: String, @Body car: Car):Call<InsertCarRes>

    @GET("clientes/{id}/vehiculos")
    fun getCars(@Header("Authorization") auth: String, @Path("id") id: String):Call<MutableList<Car>>

    @DELETE("clientes/vehiculos/{plate}")
    fun deleteCar(@Header("Authorization") auth: String, @Path("plate") plate: String):Call<SimpleResponse>
}

class CarProvider(val activity: AppCompatActivity, val loading: ProgressDialog? = null) {

    private val service: CarService = RetrofitHelper.retrofit.create(CarService::class.java)

    fun getCars(callback: (cars:MutableList<Car>)->Unit){
        loading?.show()
        val req = service.getCars(SessionApp.makeToken(), SessionApp.user._id)
        req.enqueue(ProviderCallback(activity, loading){ cars ->
            callback(cars)
        })

    }

    fun insertCar(car:Car, callback: (success:Boolean, outRange:Boolean) -> Unit){
        loading?.show()
        val req = service.insertCar(SessionApp.makeToken(), car)
        req.enqueue(ProviderCallback(activity, loading){ (success, outRange) ->
            callback(success, outRange)
        })
    }

    fun deleteCar(plate:String, callback:(success:Boolean)->Unit){
        loading?.show()
        val req = service.deleteCar(SessionApp.makeToken(), plate)
        req.enqueue(ProviderCallback(activity, loading){ (success) ->
            callback(success)
        })
    }

}


