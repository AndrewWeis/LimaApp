package start.up.tracker.ui.data.entities.home

import androidx.annotation.DrawableRes

data class HomeSection(
    @DrawableRes val iconRes: Int,
    val title: String,
    val numberOfTasksInside: Int
)
