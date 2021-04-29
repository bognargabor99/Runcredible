package hu.bme.aut.android.runcredible.repository

import hu.bme.aut.android.runcredible.database.LocationDao
import hu.bme.aut.android.runcredible.database.LocationModel
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
        thread {
            val newRunId = runDao.getMaxRunningId() + 1
            runLocations.forEach { it.runningId = newRunId }
            runDao.insertNewRunning(runLocations)
        }
    }
}