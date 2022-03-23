package start.up.tracker.utils

import start.up.tracker.entities.Project
import start.up.tracker.entities.Task

object ExtendedTasksMergeFlows {

    fun mergeFlowsForExtendedTask(
        hideCompleted: Boolean,
        tasks: List<Task>,
        projects: List<Project>
    ): List<Task> {
        val tasksWithProjectData: MutableList<Task> = mutableListOf()

        tasks
            .filter { task ->
                task.completed != hideCompleted || !task.completed
            }
            .forEach { task ->
                projects.forEach { project ->
                    if (project.projectId == task.categoryId) {
                        tasksWithProjectData.add(
                            task.copy(categoryName = project.projectTitle, categoryColor = project.color)
                        )
                    }
                }
            }

        return tasksWithProjectData
    }
}
