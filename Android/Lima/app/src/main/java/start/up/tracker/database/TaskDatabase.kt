package start.up.tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.database.dao.*
import start.up.tracker.di.ApplicationScope
import start.up.tracker.entities.*
import start.up.tracker.utils.resources.ResourcesUtils
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        Task::class,
        Project::class,
        DayStat::class,
        Technique::class,
        TaskAnalytics::class,
        TaskIdToTaskAnalyticsId::class,
    ],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun techniqueDao(): TechniquesDao
    abstract fun analyticsDao(): AnalyticsDao
    abstract fun projectsDao(): ProjectsDao
    abstract fun todayTasksDao(): TodayTasksDao
    abstract fun calendarTasksDao(): CalendarTasksDao
    abstract fun upcomingTasksDao(): UpcomingTasksDao
    abstract fun taskAnalyticsDao(): TaskAnalyticsDao
    abstract fun taskIdToTaskAnalyticsIdDao(): TaskIdToTaskAnalyticsIdDao
    abstract fun eisenhowerMatrixTasksDao(): EisenhowerMatrixTasksDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val projectsDao = database.get().projectsDao()
            val techniqueDao = database.get().techniqueDao()

            applicationScope.launch {

                // DON'T DELETE. IT'S DEFAULT PROJECT
                projectsDao.insertProject(Project(projectTitle = ResourcesUtils.getString(R.string.inbox)))

                val techniques = TechniquesStorage.getTechniques().toTypedArray()
                techniqueDao.insertAllTechniques(*techniques)
            }
        }
    }
}
