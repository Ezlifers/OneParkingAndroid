package ezlife.movil.oneparkingapp.db

import java.util.*

class Reserve{
    var id:Long? = null
    lateinit var reserve:String
    lateinit var direccion:String
    lateinit var fecha:Date
    var costo:Int = 0
    var tiempo:Int = 0

    lateinit var apodo:String
    lateinit var placa:String

}