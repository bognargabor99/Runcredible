package hu.bme.aut.android.runcredible.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert
    fun insertNewRunLocations(locationModel: List<LocationModel>)

    @Insert
    fun insertNewRunEntity(entity: RunEntity)

    @Query("SELECT * FROM runs")
    fun getRunEntities(): LiveData<List<RunEntity>>

    @Query("SELECT MAX(Id) FROM runs")
    fun getMaxRunningId(): Int

    @Query("SELECT * FROM locations WHERE runningId = :runId ORDER BY time ASC")
    fun getRunning(runId: Int): List<LocationModel>

    @Query("DELETE FROM locations WHERE runningId = :runId")
    fun deleteRunningLocations(runId: Int)

    @Query("DELETE FROM runs WHERE Id = :runId")
    fun deleteRunning(runId: Int)
}