package ezlife.movil.oneparkingapp.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import ezlife.movil.oneparkingapp.data.db.model.Zone
import io.reactivex.Flowable


@Dao
interface ZoneDao {

    @Insert
    fun insert(zone: List<Zone>)

    @Query("DELETE FROM zone WHERE zone IN (:ids)")
    fun delete(ids: List<String>)

    @Query("SELECT * FROM zone WHERE zone = :id")
    fun zoneById(id: String): Zone

}