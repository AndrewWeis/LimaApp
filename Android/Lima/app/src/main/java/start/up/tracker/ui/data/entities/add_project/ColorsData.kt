package start.up.tracker.ui.data.entities.add_project

import androidx.annotation.ColorInt

/**
 * Entity списка "цвет"
 */
data class ColorsData(
    var values: List<ColorData> = emptyList(),
)

/**
 * Entity для данных "цвет"
 */
data class ColorData(
    @ColorInt var colorRes: Int,
    var isSelected: Boolean,
)
