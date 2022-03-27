package start.up.tracker.ui.data.entities.settings

import androidx.annotation.DrawableRes

data class Setting(
    val id: Int,
    @DrawableRes val imageRes: Int,
    val title: String
)
