package ezlife.movil.oneparkingapp.providers

import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.util.SessionApp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.*


data class Cash(val saldo: Long, val ultimaTransaccion: Date)

interface CashService {
    @GET("clientes/{id}/saldo")
    fun getCash(@Header("Authorization") auth: String, @Path("id") id: String): Call<Cash>
}

class CashProvider(val activity: AppCompatActivity) {

    private val service: CashService = RetrofitHelper.retrofit.create(CashService::class.java)

    fun getCash(callback: (cash: Cash) -> Unit) {
        val req = service.getCash(SessionApp.makeToken(), SessionApp.user._id)
        req.enqueue(ProviderCallback(activity) { cash ->
            callback(cash)
        })

    }

}