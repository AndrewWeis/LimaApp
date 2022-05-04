package start.up.tracker.ui.data.entities.edit_task

import start.up.tracker.ui.data.entities.tasks.ChoiceData

data class ProjectData(
    val id: Int,
    val title: String,
    val color: Int,
    val isSelected: Boolean
)

fun ProjectData.toChoiceData(): ChoiceData {
    return ChoiceData(
        id = this.id,
        title = this.title,
        isSelected = this.isSelected
    )
}
