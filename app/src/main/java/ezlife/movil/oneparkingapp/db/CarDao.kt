package ezlife.movil.oneparkingapp.db

import android.arch.persistence.room.*

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

@Dao
interface CarDao{
    @Insert
    fun insertList(cars:List<Car>)

    @Insert
    fun insert(car:Car)

    @Update
    fun update(car:Car)

    @Delete
    fun delete(car:Car)

    @Query("DELETE FROM car")
    fun deleteAll()

    @Query("SELECT * FROM car ORDER BY selected DESC, apodo")
    fun all():MutableList<Car>

    @Query("SELECT * FROM car WHERE selected = 1")
    fun selected():Car

}