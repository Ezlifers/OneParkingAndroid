package ezlife.movil.oneparkingapp.db

import android.arch.persistence.room.*
import ezlife.movil.oneparkingapp.providers.ZoneBase

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
    constructor(base:ZoneBase) : this() {
        this.zone = base._id
        this.nombre = base.nombre
        this.codigo = base.codigo
        this.direccion = base.direccion
        this.lon = base.localización.coordinates[0]
        this.lat = base.localización.coordinates[1]
    }
}

@Dao
interface ZoneDao{

    @Insert
    fun insert(zone:List<Zone>)

    @Query("DELETE FROM zone WHERE zone IN (:p0)")
    fun delete(ids:List<String>)

    @Query("SELECT * FROM zone WHERE zone = :p0")
    fun zoneById(id:String):Zone


}