package hu.bme.aut.android.runcredible.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [LocationModel::class]
)
abstract class RunningDatabase : RoomDatabase() {
    abstract fun runningDao(): LocationDao
}