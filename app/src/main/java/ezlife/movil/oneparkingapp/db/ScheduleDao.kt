package ezlife.movil.oneparkingapp.db

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import ezlife.movil.oneparkingapp.providers.Time

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
    constructor(zone: String, dia: Int, time:Time) : this() {
        this.dia = dia
        this.d = time.d
        this.ti = time.ti
        this.tf = time.tf
        this.zone = zone
    }
}

@Dao
interface ScheduleDao {

    @Insert
    fun insert(schedule: List<Schedule>)

    @Query("DELETE FROM schedule WHERE zone IN (:p0)")
    fun deleteByZone(ids:List<String>)

    @Query("SELECT * FROM schedule WHERE zone == :p0 AND dia == :p1 ORDER BY ti ASC")
    fun allByZone(zoneID: String, day: Int): List<Schedule>

    @Query("SELECT * FROM schedule WHERE zone == :p0")
    fun all(zoneID: String): List<Schedule>

}