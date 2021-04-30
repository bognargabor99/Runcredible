package hu.bme.aut.android.runcredible.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class RunEntity(
    @PrimaryKey(autoGenerate = false)
    val Id: Int,
    val time: String
)
