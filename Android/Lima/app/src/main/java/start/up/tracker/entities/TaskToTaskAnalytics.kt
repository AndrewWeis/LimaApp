package start.up.tracker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskToTaskAnalytics_table")
data class TaskToTaskAnalytics (
    @PrimaryKey(autoGenerate = false)
    val from : Int = 0,
    val to : Int = 0
)