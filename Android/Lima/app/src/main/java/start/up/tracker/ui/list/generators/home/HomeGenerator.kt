package start.up.tracker.ui.list.generators.home

import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.Header
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.home.HomeSection
import start.up.tracker.ui.data.entities.home.ProjectsData

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
