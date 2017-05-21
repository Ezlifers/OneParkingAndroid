package ezlife.movil.oneparkingapp.db

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Zone::class
        , parentColumns = arrayOf("id")
        , childColumns = arrayOf("zone_id")
        , onDelete = CASCADE)))
class Schedule() {
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "zone_id")
    var zoneId: Long? = null

    var dia: Int = 0
    var d: Boolean = true
    var ti: Int = 0
    var tf: Int = 0
    var p: Int = 0

    @Ignore
    constructor(dia: Int, d: Boolean, ti: Int, tf: Int, p: Int, zoneId: Long) : this() {
        this.dia = dia
        this.d = d
        this.ti = ti
        this.tf = tf
        this.p = p
        this.zoneId = zoneId
    }
}

@Dao
interface ScheduleDao {

    @Insert
    fun insert(schedule: List<Schedule>)

    @Query("SELECT * from schedule WHERE zone_id == :zoneId AND dia == :day")
    fun allByZone(zoneID: String, day: Int): List<Schedule>

}