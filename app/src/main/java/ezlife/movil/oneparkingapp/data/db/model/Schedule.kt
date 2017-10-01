package ezlife.movil.oneparkingapp.data.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import ezlife.movil.oneparkingapp.data.api.model.Time


@Entity()
class Schedule() {
    @PrimaryKey
    var id: Long? = null
    var zone: String? = null
    var dia: Int = 0
    var d: Boolean = true
    var ti: Int = 0
    var tf: Int = 0

    @Ignore
    constructor(zone: String, dia: Int, time: Time) : this() {
        this.dia = dia
        this.d = time.d
        this.ti = time.ti
        this.tf = time.tf
        this.zone = zone
    }
}