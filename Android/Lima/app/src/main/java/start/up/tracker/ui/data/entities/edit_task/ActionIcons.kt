package start.up.tracker.ui.data.entities.edit_task

import androidx.annotation.DrawableRes

data class ActionIcons(
    val icons: List<ActionIcon>
)

data class ActionIcon(
    val id: Int,
    @DrawableRes val iconRes: Int,
) {
    companion object Ids {
        const val ICON_PRIORITY = 1
        const val ICON_DATE = 2
        const val ICON_TIME_START = 3
        const val ICON_TIME_END = 4
        const val ICON_PROJECTS = 5
        const val ICON_REPEATS = 6

        const val ICON_POMODORO = 7
    }
}
