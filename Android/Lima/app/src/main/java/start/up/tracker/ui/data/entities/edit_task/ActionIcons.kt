package start.up.tracker.ui.data.entities.edit_task

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import start.up.tracker.R

data class ActionIcons(
    val icons: List<ActionIcon>
)

data class ActionIcon(
    val id: Int,
    @DrawableRes val iconRes: Int,
    @ColorRes val iconColor: Int = R.color.gray_B7B6B7
) {
    companion object Ids {
        const val ICON_PRIORITY = 1
        const val ICON_DATE = 2
        const val ICON_TIME_START = 3
        const val ICON_TIME_END = 4
        const val ICON_PROJECTS = 5
        const val ICON_REPEATS = 6
        
        const val ICON_NOTIFICATIONS = 7
        const val ICON_POMODORO = 8
        const val ICON_EISENHOWER_MATRIX = 9
    }
}
