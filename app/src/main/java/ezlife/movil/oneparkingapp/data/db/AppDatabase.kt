package ezlife.movil.oneparkingapp.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.model.Car


@Database(version = 1, entities = arrayOf(Car::class))
abstract class AppDatabase: RoomDatabase(){
    abstract fun carDao(): CarDao
}