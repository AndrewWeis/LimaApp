package start.up.tracker.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import start.up.tracker.data.models.Category
import start.up.tracker.data.models.Task

/**
 * This helper class helps us to query single task and get all the categories that this task is associated with
 */
data class TaskWithCategories (
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskName",
        entityColumn = "categoryName",
        associateBy = Junction(TaskCategoryCrossRef::class)
    )
    val categories: List<Category>
)