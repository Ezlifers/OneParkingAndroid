package ezlife.movil.oneparkingapp.data.observer

import io.reactivex.subjects.PublishSubject

class MarkObserver{

    val markerState:PublishSubject<MarkerState> = PublishSubject.create()
    val makerBusy:PublishSubject<MarkerBusy> = PublishSubject.create()

}

data class MarkerState(val id:String, val state:Int)
data class MarkerBusy(val type:Int, val dis:Boolean)
