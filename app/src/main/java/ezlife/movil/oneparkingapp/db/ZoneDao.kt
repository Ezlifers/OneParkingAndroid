package ezlife.movil.oneparkingapp.db

import android.arch.persistence.room.*

@Entity
class Zone() {
    @PrimaryKey
    var id: Long? = null
    lateinit var zone: String
    lateinit var nombre: String
    var codigo: Int = 0
    lateinit var direccion: String
    var tiempoMax: Int = 0
    var tiempoMin: Int = 0
    var lon: Double = 0.0
    var lat: Double = 0.0

    @Ignore
    constructor(zone: String, nombre: String, codigo: Int, direccion: String, lat: Double, lon: Double, tiempoMax: Int, tiempoMin: Int) : this() {
        this.zone = zone
        this.nombre = nombre
        this.codigo = codigo
        this.direccion = direccion
        this.lon = lon
        this.lat = lat
        this.tiempoMax = tiempoMax
        this.tiempoMin = tiempoMin
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