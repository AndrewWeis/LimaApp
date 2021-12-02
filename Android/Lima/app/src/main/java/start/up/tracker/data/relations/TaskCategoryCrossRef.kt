package start.up.tracker.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["taskName", "categoryName"], tableName = "cross_ref")
data class TaskCategoryCrossRef(
    val taskName: String,
    val categoryName: String
)