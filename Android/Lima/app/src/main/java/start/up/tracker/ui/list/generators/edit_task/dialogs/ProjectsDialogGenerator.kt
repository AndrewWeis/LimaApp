package start.up.tracker.ui.list.generators.edit_task.dialogs

import start.up.tracker.R
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.ui.data.entities.edit_task.ProjectData
import start.up.tracker.ui.data.entities.edit_task.toChoiceData

class ProjectsDialogGenerator {

    fun getProjectsListItems(projects: List<ProjectData>): List<ListItem> {
        return projects.map {
            val settings = Settings(
                icon = R.drawable.ic_project_circle,
                iconColor = it.color,
                width = 12,
                height = 12
            )
            ListItem(data = it.toChoiceData(), settings = settings)
        }
    }
}
