package ezlife.movil.oneparkingapp.providers

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.util.SessionApp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IncidentService {

    @POST("incidencias")
    fun notifyIncident(@Header("Authorization") auth: String, @Body req: Incident): Call<NotifyIncidentRes>
}

class IncidentProvider(val activity: AppCompatActivity, val loading: ProgressDialog? = null) {

    private val service: IncidentService = RetrofitHelper.retrofit.create(IncidentService::class.java)

    fun notifyIncident(incident: Incident, callback: (success: Boolean, failImage: Boolean) -> Unit) {
        loading?.show()
        val req = service.notifyIncident(SessionApp.makeToken(), incident)
        req.enqueue(ProviderCallback(activity, loading) { (success, failImage) ->
            callback(success, failImage)
        })
    }

}

data class UserIncident(val nombre: String, val celular: String)
data class ZoneIncident(val id: String, val codigo: Int, val nombre: String)
data class Incident(val foto: String, val observaciones: String, val zona: ZoneIncident, val usuario: UserIncident)

data class NotifyIncidentRes(val success: Boolean, val failImage: Boolean)