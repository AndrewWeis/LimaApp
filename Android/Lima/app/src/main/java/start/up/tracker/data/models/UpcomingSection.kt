package start.up.tracker.data.models

data class UpcomingSection(
    val section: String,
    val tasksList: List<TodayTask>
)