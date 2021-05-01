package hu.bme.aut.android.runcredible.repository

import androidx.lifecycle.LiveData
import hu.bme.aut.android.runcredible.database.LocationDao
import hu.bme.aut.android.runcredible.database.LocationModel
import hu.bme.aut.android.runcredible.database.RunEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class RunningRepository(private val runDao: LocationDao) {

    fun getAll(): List<LocationModel> {
        val ret = mutableListOf<LocationModel>()
        thread { ret.addAll(runDao.getAllLocations()) }
        return ret
    }

    fun getRunning(runId: Int): List<LocationModel> {
        val ret = mutableListOf<LocationModel>()
        thread { ret.addAll(runDao.getRunning(runId)) }
        return ret
    }

    fun insertNewRunning(runLocations: List<LocationModel>) {
        val newRunId = runDao.getMaxRunningId() + 1
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
        val runEntity = RunEntity(newRunId, dateString)
        runLocations.forEach { it.runningId = newRunId }
        runDao.insertNewRunEntity(runEntity)
        runDao.insertNewRunLocations(runLocations)
    }

    fun getRunEntities(): LiveData<List<RunEntity>> {
        return runDao.getRunEntities()
    }

    fun delete(run: RunEntity) {
        GlobalScope.launch {
            runDao.deleteRunning(run.Id)
            runDao.deleteRunningLocations(run.Id)
        }
    }
}