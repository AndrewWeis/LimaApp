package start.up.tracker.ui.data.entities.chips

/**
 * Entity списка "chip"
 */
data class ChipsData(
    var values: List<ChipData> = emptyList(),
)

/**
 * Entity для данных "chip"
 */
data class ChipData(
    var id: Int,
    var name: String,
    var isSelected: Boolean = false,
)
