package ezlife.movil.oneparkingapp

import android.app.Application
import android.arch.persistence.room.Room
import ezlife.movil.oneparkingapp.db.AppDataBase
import ezlife.movil.oneparkingapp.db.DB

class App:Application(){

    override fun onCreate() {
        super.onCreate()
        DB.con = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "oneparkingapp").build()
    }

}
