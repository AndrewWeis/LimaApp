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
    }
}
