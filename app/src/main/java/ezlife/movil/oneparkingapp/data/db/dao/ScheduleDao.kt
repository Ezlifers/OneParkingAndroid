package ezlife.movil.oneparkingapp.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import ezlife.movil.oneparkingapp.data.db.model.Schedule


@Dao
interface ScheduleDao {

    @Insert
    fun insert(schedule: List<Schedule>)

    @Query("DELETE FROM schedule WHERE zone IN (:ids)")
    fun deleteByZone(ids: List<String>)

    @Query("SELECT * FROM schedule WHERE zone == :zoneID AND dia == :day ORDER BY ti ASC")
    fun allByZone(zoneID: String, day: Int): List<Schedule>

    @Query("SELECT * FROM schedule WHERE zone == :zoneID")
    fun all(zoneID: String): List<Schedule>

}