package hu.bme.aut.android.runcredible.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert
    fun insertNewRunLocations(locationModel: List<LocationModel>)

    @Insert
    fun insertNewRunEntity(entity: RunEntity)

    @Query("SELECT * FROM locations")
    fun getAllLocations() : List<LocationModel>

    @Query("SELECT * FROM runs")
    fun getRunEntities(): List<RunEntity>

    @Query("SELECT MAX(Id) FROM runs")
    fun getMaxRunningId(): Int

    @Query("SELECT * FROM locations WHERE runningId = :runId ORDER BY time ASC")
    fun getRunning(runId: Int): List<LocationModel>

    @Query("DELETE FROM locations WHERE runningId = :runId")
    fun deleteRunning(runId: Int)
}