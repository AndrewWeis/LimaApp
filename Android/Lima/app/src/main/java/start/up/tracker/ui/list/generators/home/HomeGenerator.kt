package start.up.tracker.ui.list.generators.home

import start.up.tracker.R
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.header.Header
import start.up.tracker.ui.data.entities.home.HomeSection
import start.up.tracker.ui.data.entities.home.ProjectsData
import start.up.tracker.utils.resources.ResourcesUtils

class HomeGenerator {

    fun createInboxSectionListItem(homeSection: HomeSection): ListItem {
        return ListItem(
            id = ListItemIds.INBOX,
            type = ListItemTypes.HOME_BLOCK,
            data = homeSection
        )
    }

    fun createTodaySectionListItem(homeSection: HomeSection): ListItem {
        return ListItem(
            id = ListItemIds.TODAY,
            type = ListItemTypes.HOME_BLOCK,
            data = homeSection
        )
    }

    fun createUpcomingSectionListItem(homeSection: HomeSection): ListItem {
        return ListItem(
            id = ListItemIds.UPCOMING,
            type = ListItemTypes.HOME_BLOCK,
            data = homeSection
        )
    }

    fun createPomodoroSectionListItem(): ListItem {
        val pomodoroSection = HomeSection(
            iconRes = R.drawable.ic_timer,
            title = ResourcesUtils.getString(R.string.title_pomodoro_timer)
        )

        return ListItem(
            id = ListItemIds.POMODORO,
            type = ListItemTypes.HOME_BLOCK,
            data = pomodoroSection
        )
    }

    fun createEisenhowerMatrixSectionListItem(): ListItem {
        val eisenhowerMatrixSection = HomeSection(
            iconRes = R.drawable.ic_eisenhower_matrix,
            title = ResourcesUtils.getString(R.string.title_eisenhower_matrix)
        )

        return ListItem(
            id = ListItemIds.EISENHOWER_MATRIX,
            type = ListItemTypes.HOME_BLOCK,
            data = eisenhowerMatrixSection
        )
    }

    fun createProjectsListItems(projectsData: ProjectsData): ListItem {
        return ListItem(
            id = ListItemIds.PROJECTS,
            type = ListItemTypes.LIST,
            data = projectsData
        )
    }

    fun createHeaderListItem(header: Header): ListItem {
        return ListItem(
            id = ListItemIds.PROJECTS,
            type = ListItemTypes.HEADER,
            data = header
        )
    }
}
