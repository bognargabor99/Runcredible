package hu.bme.aut.android.runcredible

import android.app.Application
import androidx.room.Room
import hu.bme.aut.android.runcredible.database.RunningDatabase

class RunningApplication : Application() {

    companion object {
        lateinit var runningDatabase: RunningDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        runningDatabase = Room.databaseBuilder(
            applicationContext,
            RunningDatabase::class.java,
            "runningDB"
        ).fallbackToDestructiveMigration().build()
    }

}