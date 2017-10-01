package ezlife.movil.oneparkingapp.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import ezlife.movil.oneparkingapp.data.db.model.Config


@Dao
interface ConfigDao {
    @Insert
    fun insert(config: Config)

    @Query("DELETE FROM config")
    fun delete()

    @Query("SELECT * FROM config LIMIT 1")
    fun config(): Config
}