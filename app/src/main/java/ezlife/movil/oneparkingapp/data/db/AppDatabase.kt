package ezlife.movil.oneparkingapp.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.dao.ConfigDao
import ezlife.movil.oneparkingapp.data.db.dao.ScheduleDao
import ezlife.movil.oneparkingapp.data.db.dao.ZoneDao
import ezlife.movil.oneparkingapp.data.db.model.Car
import ezlife.movil.oneparkingapp.data.db.model.Config
import ezlife.movil.oneparkingapp.data.db.model.Schedule
import ezlife.movil.oneparkingapp.data.db.model.Zone

class Converters {

    companion object {
        @JvmStatic
        @TypeConverter
        fun fromListInt(value: List<Int>): String = value.joinToString(",")

        @JvmStatic
        @TypeConverter
        fun toListInt(value: String): List<Int> = value.split(",").map { it.toInt() }
    }

}


@Database(version = 2, entities = arrayOf(Car::class, Config::class, Zone::class, Schedule::class))
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun configDao(): ConfigDao
    abstract fun zoneDao(): ZoneDao
    abstract fun scheduleDao(): ScheduleDao
}