package start.up.tracker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskIdToTaskAnalyticsId_table")
data class TaskIdToTaskAnalyticsId (
    @PrimaryKey(autoGenerate = false)
    val from : Int = 0,
    val to : Int = 0
)