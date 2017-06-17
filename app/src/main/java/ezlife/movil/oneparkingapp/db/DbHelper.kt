package ezlife.movil.oneparkingapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(version = 1, entities = arrayOf(Car::class, Zone::class, Schedule::class))
abstract class AppDataBase:RoomDatabase(){
    abstract fun carDao():CarDao
    abstract fun zoneDao():ZoneDao
    abstract fun scheduleDao():ScheduleDao
}

object DB{
    lateinit var con:AppDataBase
}