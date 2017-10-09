package ezlife.movil.oneparkingapp.ui.info.prices

import android.arch.lifecycle.ViewModel
import ezlife.movil.oneparkingapp.data.db.dao.ConfigDao
import ezlife.movil.oneparkingapp.util.applyShedures
import io.reactivex.Observable
import javax.inject.Inject

class PricesViewModel @Inject constructor(private val dao: ConfigDao) : ViewModel() {

    fun getSetupPrice(): Observable<Pair<Int, List<Int>>> =
            Observable.fromCallable { dao.config() }
                    .map { Pair(it.tiempoMin / 60, it.precio) }
                    .applyShedures()


}