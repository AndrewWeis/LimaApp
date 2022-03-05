package start.up.tracker.data.entities

data class UpcomingSection(
    val section: String,
    val tasksList: List<ExtendedTask>
)