package hu.bme.aut.android.runcredible.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert
    fun insertNewRunning(locationModel: List<LocationModel>)

    @Query("SELECT * FROM locations")
    fun getAllLocations() : List<LocationModel>

    @Query("SELECT DISTINCT runningId FROM locations")
    fun getRunningIds(): List<Int>

    @Query("SELECT MAX(runningId) FROM locations")
    fun getMaxRunningId(): Int

    @Query("SELECT * FROM locations WHERE runningId = :runId ORDER BY time ASC")
    fun getRunning(runId: Int): List<LocationModel>

    @Query("DELETE FROM locations WHERE runningId = :runId")
    fun deleteRunning(runId: Int)
}