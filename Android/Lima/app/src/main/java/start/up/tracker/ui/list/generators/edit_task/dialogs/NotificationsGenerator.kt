package start.up.tracker.ui.list.generators.edit_task.dialogs


import start.up.tracker.R
import start.up.tracker.entities.NotificationIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.tasks.ChoiceData
import start.up.tracker.utils.resources.ResourcesUtils

class NotificationsGenerator {

    fun getDefaultNotificationListItems(selectedNotificationId: Int): List<ListItem> {
        val listItems: MutableList<ListItem> = mutableListOf()
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.NO_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.AT_TASK_TIME_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.FIVE_MINUTES_BEFORE_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.TEN_MINUTES_BEFORE_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.FIFTEEN_MINUTES_BEFORE_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.THIRTY_MINUTES_BEFORE_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.ONE_HOUR_BEFORE_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.TWO_HOURS_BEFORE_NOTIFICATION))
        listItems.add(createNotificationListItem(selectedId = selectedNotificationId, notificationId = NotificationIds.ONE_DAY_BEFORE_NOTIFICATION))
        return listItems
    }

    private fun createNotificationListItem(selectedId: Int, notificationId: Int): ListItem {
        var title = ""
        when (notificationId) {
            NotificationIds.NO_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_none)
            }
            NotificationIds.AT_TASK_TIME_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_task_time)
            }
            NotificationIds.FIVE_MINUTES_BEFORE_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_5_before)
            }
            NotificationIds.TEN_MINUTES_BEFORE_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_10_before)
            }
            NotificationIds.FIFTEEN_MINUTES_BEFORE_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_15_before)
            }
            NotificationIds.THIRTY_MINUTES_BEFORE_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_30_before)
            }
            NotificationIds.ONE_HOUR_BEFORE_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_1_hour_before)
            }
            NotificationIds.TWO_HOURS_BEFORE_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_2_hour_before)
            }
            NotificationIds.ONE_DAY_BEFORE_NOTIFICATION -> {
                title = ResourcesUtils.getString(R.string.notification_1_day_before)
            }
        }

        val data = ChoiceData(
            id = notificationId,
            title = title,
            isSelected = notificationId == selectedId
        )

        return ListItem(data = data)
    }
}