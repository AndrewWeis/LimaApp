package start.up.tracker.ui.list.generators.edit_task.dialogs


import start.up.tracker.entities.NotificationType
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.tasks.ChoiceData
import start.up.tracker.utils.resources.ResourcesUtils

class NotificationsGenerator {

    fun getDefaultNotificationListItems(selectedNotificationType: NotificationType): List<ListItem> {
        val listItems: MutableList<ListItem> = mutableListOf()
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.NONE))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.AT_TASK_TIME))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.FIVE_MINUTES_BEFORE))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.TEN_MINUTES_BEFORE))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.FIFTEEN_MINUTES_BEFORE))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.THIRTY_MINUTES_BEFORE))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.ONE_HOUR_BEFORE))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.TWO_HOURS_BEFORE))
        listItems.add(createNotificationListItem(selectedType = selectedNotificationType, requiredType = NotificationType.ONE_DAY_BEFORE))
        return listItems
    }

    private fun createNotificationListItem(selectedType: NotificationType, requiredType: NotificationType): ListItem {
        val data = ChoiceData(
            id = requiredType.typeId,
            title = ResourcesUtils.getString(requiredType.resourceId),
            isSelected = requiredType == selectedType
        )

        return ListItem(data = data)
    }
}
