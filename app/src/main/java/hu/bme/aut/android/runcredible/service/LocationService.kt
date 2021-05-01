package hu.bme.aut.android.runcredible.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import hu.bme.aut.android.runcredible.MainActivity
import hu.bme.aut.android.runcredible.R
import hu.bme.aut.android.runcredible.RunningApplication
import hu.bme.aut.android.runcredible.database.LocationModel
import hu.bme.aut.android.runcredible.database.RunningDatabase
import hu.bme.aut.android.runcredible.location.LocationHelper
import hu.bme.aut.android.runcredible.repository.RunningRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.round

class LocationService : Service() {
    private val thisRunLocations = mutableListOf<LocationModel>()
    private lateinit var repository: RunningRepository

    companion object {
        private const val STOP_FILTER = "STOP_RUNCREDIBLE_MONITORING"
        private const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "ForegroundServiceChannel"
        var isRunning: Boolean = false
    }

    private var locationHelper: LocationHelper? = null

    var lastLocation: Location? = null
        private set

    override fun onCreate() {
        super.onCreate()

        repository = RunningRepository(RunningApplication.runningDatabase.runningDao())
        Log.d("locService", "Opened database, created repository")
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action.equals(STOP_FILTER)) {
            Log.d("locService", "Stopping service")
            stopSelf()
        }

        startForeground(NOTIFICATION_ID, createNotification())
        isRunning = true
        if (locationHelper == null) {
            val helper = LocationHelper(applicationContext, LocationServiceCallback())
            helper.startLocationMonitoring()
            locationHelper = helper
        }
        return START_STICKY
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

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        createNotificationChannel()

        val contentIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)

        val stopIntent = Intent(this, LocationService::class.java)
        stopIntent.action = STOP_FILTER

        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.directions_run)
                .setContentIntent(contentIntent)
                .addAction(0, getString(R.string.stop), stopPendingIntent)
                .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        locationHelper?.stopLocationMonitoring()

        isRunning = false

        if (thisRunLocations.isNotEmpty()) {
            GlobalScope.launch { repository.insertNewRunning(thisRunLocations) }
            Log.d("locService", "Inserted new running")
            Log.d("locService", thisRunLocations.toString())
        }
        else
            Log.d("locService", "Not inserted")

        super.onDestroy()
    }
}