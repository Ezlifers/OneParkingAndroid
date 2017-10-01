package ezlife.movil.oneparkingapp.data.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import ezlife.movil.oneparkingapp.data.api.model.ZoneBase


@Entity
class Zone() {
    @PrimaryKey
    var id: Long? = null
    lateinit var zone: String
    lateinit var nombre: String
    var codigo: Int = 0
    lateinit var direccion: String
    var lon: Double = 0.0
    var lat: Double = 0.0

    @Ignore
    constructor(base: ZoneBase) : this() {
        this.zone = base._id
        this.nombre = base.nombre
        this.codigo = base.codigo
        this.direccion = base.direccion
        this.lon = base.localizacion.coordinates[0]
        this.lat = base.localizacion.coordinates[1]
    }
}