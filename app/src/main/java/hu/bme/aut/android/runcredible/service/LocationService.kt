package hu.bme.aut.android.runcredible.service

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import hu.bme.aut.android.runcredible.database.LocationModel
import hu.bme.aut.android.runcredible.database.RunningDatabase
import hu.bme.aut.android.runcredible.location.LocationHelper
import hu.bme.aut.android.runcredible.repository.RunningRepository
import kotlin.math.round

class LocationService : Service() {
    private val thisRunLocations = mutableListOf<LocationModel>()
    private lateinit var database: RunningDatabase
    private lateinit var repository: RunningRepository

    companion object {
        private const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "ForegroundServiceChannel"
        var isRunning: Boolean = false
    }

    private var locationHelper: LocationHelper? = null

    var lastLocation: Location? = null
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
                this,
                RunningDatabase::class.java,
                "runningDB")
                .build()

        repository = RunningRepository(database.runningDao())
        Log.d("locService", "Opened database, created repository")
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true
        if (locationHelper == null) {
            val helper = LocationHelper(applicationContext, LocationServiceCallback())
            helper.startLocationMonitoring()
            locationHelper = helper
        }

        val handler = Looper.getMainLooper()

        handler.run {
            Toast.makeText(this@LocationService, "started", Toast.LENGTH_SHORT).show()
        }

        return Service.START_STICKY
    }

    inner class LocationServiceCallback : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return

            lastLocation = location

            val locModel = LocationModel(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    height = location.altitude,
                    time = location.time
            )

            thisRunLocations.add(locModel)
            Log.d("locService", "Added new location to this running route")
            Log.d("locService", "lat: ${round(locModel.latitude * 100.0) / 100.0}, lng: ${round(locModel.longitude * 100.0) / 100.0}")
        }
    }

    override fun onDestroy() {
        locationHelper?.stopLocationMonitoring()
        val handler = Looper.getMainLooper()

        isRunning = false

        handler.run {
            Toast.makeText(this@LocationService, "destroyed", Toast.LENGTH_SHORT).show()
        }

        if (thisRunLocations.isNotEmpty()) {
            repository.insertNewRunning(thisRunLocations)
            Log.d("locService", "Inserted new running")
            Log.d("locService", thisRunLocations.toString())
        }
        else
            Log.d("locService", "Not inserted")
        super.onDestroy()
    }
}