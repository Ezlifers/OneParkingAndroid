package ezlife.movil.oneparkingapp.data.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Config {
    @PrimaryKey
    var id: Long? = null
    var vehiculosUsuario: Int = 0
    var tiempoMin: Int = 0
    var tiempoMax: Int = 0
    var precio: List<Int> = emptyList()

}