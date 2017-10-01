package ezlife.movil.oneparkingapp.data.api.model

import java.util.Date

data class CurrentState(val libre: Date, val bahias: Int, var bahiasOcupadas: Int, val dis: Int, var disOcupadas: Int)
class ZoneState(val _id:String, var tipo:Int, val localizacion: Point, var estado:CurrentState?){
    companion object {
        @JvmStatic
        val FREE:Int = 0
        @JvmStatic
        val TARIFICATION:Int = 1
        @JvmStatic
        val PROHIBITED:Int = 2
    }
}