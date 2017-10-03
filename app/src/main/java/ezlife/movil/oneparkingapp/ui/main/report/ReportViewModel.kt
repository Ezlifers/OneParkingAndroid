package ezlife.movil.oneparkingapp.ui.main.report

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.widget.ImageView
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.api.IncidentApi
import ezlife.movil.oneparkingapp.data.api.model.Incident
import ezlife.movil.oneparkingapp.data.api.model.IncidentRes
import ezlife.movil.oneparkingapp.data.api.model.UserIncident
import ezlife.movil.oneparkingapp.data.api.model.ZoneIncident
import ezlife.movil.oneparkingapp.data.prefs.UserSession
import ezlife.movil.oneparkingapp.util.applyShedures


import ezlife.movil.oneparkingauxiliar.util.getBytesFromBitmap
import io.reactivex.Observable
import javax.inject.Inject

class ReportViewModel @Inject constructor(private val api: IncidentApi,
                                          private val session: UserSession,
                                          private val context: Context) : ViewModel() {

    fun notifyIncident(id: String, code: Int, name: String,
                       view: ImageView, msg: String): Observable<Unit> = Observable.create<Incident> {

        val drawable: BitmapDrawable = view.drawable as BitmapDrawable
        val imageData = getBytesFromBitmap(drawable.bitmap)
        val img = Base64.encodeToString(imageData, Base64.DEFAULT)
        val zoneIncident = ZoneIncident(id, code, name)
        val user = UserIncident(session.userName, session.userCel)
        it.onNext(Incident(img, msg, zoneIncident, user))
    }
            .flatMap { api.notifyIncident(session.makeToken(), it) }
            .flatMap { res ->
                Observable.create<Unit> {
                    when {
                        res.success -> it.onNext(Unit)
                        res.failImage -> it.onError(Throwable(context.getString(R.string.report_fail_image)))
                        else -> it.onError(Throwable(context.getString(R.string.report_fail)))
                    }
                    it.onComplete()
                }
            }
            .applyShedures()

}