package start.up.tracker.utils

import start.up.tracker.data.entities.Category
import start.up.tracker.data.entities.Task

object ExtendedTasksMergeFlows {

    fun mergeFlowsForExtendedTask(
        hideCompleted: Boolean,
        tasks: List<Task>,
        categories: List<Category>
    ): List<Task> {
        val tasksWithCategoryData: MutableList<Task> = mutableListOf()

        tasks
            .filter { task ->
                task.completed == hideCompleted
            }
            .forEach { task ->
                categories.forEach { category ->
                    if (category.categoryId == task.categoryId) {
                        tasksWithCategoryData.add(
                            task.copy(categoryName = category.categoryName, color = category.color)
                        )
                    }
                }
            }

        return tasksWithCategoryData
    }
}
