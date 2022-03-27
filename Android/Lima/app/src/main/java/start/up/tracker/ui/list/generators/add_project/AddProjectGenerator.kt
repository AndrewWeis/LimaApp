package start.up.tracker.ui.list.generators.add_project

import start.up.tracker.entities.Project
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.add_project.ColorsData

class AddProjectGenerator {

    fun createActionsListItem(isEnabled: Boolean): ListItem {
        return ListItem(
            type = ListItemTypes.HEADER,
            data = isEnabled
        )
    }

    fun createTitleListItem(project: Project): ListItem {
        return ListItem(
            type = ListItemTypes.INPUT_TEXT,
            data = project.projectTitle
        )
    }

    fun createColorsDataListItem(colorsData: ColorsData): ListItem {
        return ListItem(
            type = ListItemTypes.LIST,
            data = colorsData
        )
    }
}
