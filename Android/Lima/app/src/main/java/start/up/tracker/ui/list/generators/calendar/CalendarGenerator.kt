package start.up.tracker.ui.list.generators.calendar

import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.calendar.CalendarTasksData
import start.up.tracker.ui.data.entities.calendar.CalendarTimesData
import start.up.tracker.ui.list.generators.base.BaseGenerator

class CalendarGenerator : BaseGenerator() {

    fun createTimesListItem(times: CalendarTimesData): ListItem {
        return ListItem(
            id = ListItemIds.CALENDAR_TIMES,
            type = ListItemTypes.LIST,
            data = times
        )
    }

    fun createTasksListItem(tasks: CalendarTasksData): ListItem {
        return ListItem(
            id = ListItemIds.CALENDAR_TASKS,
            type = ListItemTypes.LIST,
            data = tasks
        )
    }
}
