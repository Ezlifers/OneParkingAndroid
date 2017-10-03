package ezlife.movil.oneparkingapp.data.api

import ezlife.movil.oneparkingapp.data.api.model.Incident
import ezlife.movil.oneparkingapp.data.api.model.IncidentRes

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IncidentApi{

    @POST("incidencias")
    fun notifyIncident(@Header("Authorization") auth: String, @Body req: Incident): Observable<IncidentRes>

}
