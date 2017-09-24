package ezlife.movil.oneparkingapp.data.db.dao

import android.arch.persistence.room.*
import ezlife.movil.oneparkingapp.data.db.model.Car


@Dao
interface CarDao{
    @Insert
    fun insertList(cars:List<Car>)

    @Insert
    fun insert(car: Car)

    @Update
    fun update(car: Car)

    @Delete
    fun delete(car: Car)

    @Query("DELETE FROM car")
    fun deleteAll()

    @Query("SELECT * FROM car ORDER BY selected DESC, apodo")
    fun all():MutableList<Car>

    @Query("SELECT * FROM car WHERE selected = 1")
    fun selected(): Car

}