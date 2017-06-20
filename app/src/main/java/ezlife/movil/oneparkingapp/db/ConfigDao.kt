package ezlife.movil.oneparkingauxiliar.db

import android.arch.persistence.room.*

@Entity
class Config {
    @PrimaryKey
    var id:Long? = null
    var vehiculosUsuario:Int = 0
    var tiempoMin:Int = 0
    var tiempoMax:Int = 0
    var preciosText:String? = null

    @Ignore
    var precio:List<Int> = emptyList()
        get(){
            if(field.isEmpty()){
                field = preciosText?.split(",")?.map { it.toInt() } ?: emptyList()
            }
            return field
        }

}


@Dao
interface ConfigDao{
    @Insert
    fun insert(config:Config)

    @Query("DELETE FROM config")
    fun delete()

    @Query("SELECT * FROM config LIMIT 1")
    fun config():Config
}