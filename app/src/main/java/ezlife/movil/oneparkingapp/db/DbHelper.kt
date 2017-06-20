package ezlife.movil.oneparkingapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ezlife.movil.oneparkingauxiliar.db.Config
import ezlife.movil.oneparkingauxiliar.db.ConfigDao

@Database(version = 1, entities = arrayOf(Car::class, Zone::class, Schedule::class, Config::class))
abstract class AppDataBase:RoomDatabase(){
    abstract fun carDao():CarDao
    abstract fun zoneDao():ZoneDao
    abstract fun scheduleDao():ScheduleDao
    abstract fun configDao():ConfigDao
}

object DB{
    lateinit var con:AppDataBase
}