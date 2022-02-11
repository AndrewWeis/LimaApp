package start.up.tracker.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import start.up.tracker.data.entities.Category
import start.up.tracker.data.entities.Task

data class CategoryWithTasks (
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "taskId",
        associateBy = Junction(TaskCategoryCrossRef::class)
    )
    val tasks: List<Task>
)