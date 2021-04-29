package hu.bme.aut.android.runcredible.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationModel(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        var runningId: Int = 0,
        val latitude: Double,
        val longitude: Double,
        val height: Double,
        val time: Long
)