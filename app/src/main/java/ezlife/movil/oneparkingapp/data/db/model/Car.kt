package ezlife.movil.oneparkingapp.data.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
class Car(){
    @PrimaryKey
    var id:Long? = null
    lateinit var placa:String
    lateinit var apodo:String
    lateinit var marca:String
    var selected:Boolean? = null

    @Ignore
    constructor(placa:String, apodo:String, marca:String, selected:Boolean? = null) : this() {
        this.placa = placa
        this.apodo = apodo
        this.marca = marca
        this.selected = selected
    }
}
