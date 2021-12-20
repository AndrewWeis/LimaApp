package start.up.tracker.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.models.Category
import start.up.tracker.data.models.DayStat
import start.up.tracker.data.models.Task
import start.up.tracker.data.models.ExtendedTask
import start.up.tracker.data.relations.CategoryWithTasks
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.data.relations.TaskWithCategories

/**
 * A suspending function is simply a function that can be paused and resumed at a later time.
 * They can execute a long running operation and wait for it to complete without blocking.
 *
 * A flow is a stream of multiple, asynchronously computed values. Flows emit values as soon as
 * they are done computing them. A flow consists of a producer and a consumer. As the names suggest,
 * a producer emits values while the consumer receives the values. A flow can only be used or collected
 * inside a coroutine. That's why we don't need suspend modifier.
 */
@Dao
interface TaskDao {

    @Query(
        """
       SELECT * 
       FROM task_table
       JOIN cross_ref ON task_table.taskId = cross_ref.taskId
       WHERE categoryId = :categoryId AND
       (completed != :hideCompleted OR completed = 0) AND 
       task_table.taskName LIKE '%' || :searchQuery || '%' 
       ORDER BY priority 
       ASC, created"""
    )
    fun getTasksOfCategory(
        searchQuery: String,
        hideCompleted: Boolean,
        categoryId: Int
    ): Flow<List<Task>>

    @Query(
        """
        SELECT COUNT(*) 
        FROM task_table 
        JOIN cross_ref ON task_table.taskId = cross_ref.taskId
        WHERE categoryId = :categoryId AND
        (completed != :hideCompleted OR completed = 0)
    """
    )
    suspend fun countTasksOfCategory(categoryId: Int, hideCompleted: Boolean): Int

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table 
        JOIN cross_ref ON task_table.taskId = cross_ref.taskId
        WHERE categoryId = 1 AND
        completed = 0
    """
    )
    fun countTasksOfInbox(): Flow<Int>

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE date = :today AND
        completed = 0
    """
    )
    fun countTodayTasks(today: String): Flow<Int>

    @Query(
        """
        SELECT 
	        task_table.taskId, task_table.taskName, task_table.taskDesc, task_table.priority, task_table.completed, task_table.created, task_table.dateLong,
            task_table.date, task_table.timeStart, task_table.timeEnd, task_table.timeStartInt, task_table.timeEndInt, task_table.wasCompleted,
	        Category.categoryId, Category.categoryName, Category.color, Category.tasksInside
        FROM cross_ref
        JOIN task_table ON task_table.taskId = cross_ref.taskId
        JOIN Category ON Category.categoryId = cross_ref.categoryId
        WHERE task_table.date = :today AND
       (completed != :hideCompleted OR completed = 0)
       ORDER BY priority 
       ASC
    """
    )
    fun getTodayTasks(today: String, hideCompleted: Boolean): Flow<List<ExtendedTask>>

    @Query(
        """
       SELECT 
            task_table.taskId, task_table.taskName, task_table.taskDesc, task_table.priority, task_table.completed, task_table.created, task_table.dateLong,
            task_table.date, task_table.timeStart, task_table.timeEnd, task_table.timeStartInt, task_table.timeEndInt, task_table.wasCompleted,
	        Category.categoryId, Category.categoryName, Category.color, Category.tasksInside
        FROM cross_ref
        JOIN task_table ON task_table.taskId = cross_ref.taskId
        JOIN Category ON Category.categoryId = cross_ref.categoryId
       WHERE task_table.date = :today AND
       task_table.timeStart != "No time" AND
       task_table.timeEnd != "No time" AND
       (completed != :hideCompleted OR completed = 0)
       ORDER BY task_table.timeEndInt
       ASC
    """
    )

    fun getCalendarTasks(today: String, hideCompleted: Boolean): Flow<List<ExtendedTask>>

    @Query(
        """
        SELECT
            task_table.taskId, task_table.taskName, task_table.taskDesc, task_table.priority, task_table.completed, task_table.created, task_table.dateLong,
            task_table.date, task_table.timeStart, task_table.timeEnd, task_table.timeStartInt, task_table.timeEndInt, task_table.wasCompleted,
            Category.categoryId, Category.categoryName, Category.color, Category.tasksInside
        FROM cross_ref
        JOIN task_table ON task_table.taskId = cross_ref.taskId
        JOIN Category ON Category.categoryId = cross_ref.categoryId
        WHERE task_table.dateLong > :today AND
        (completed != :hideCompleted OR completed = 0)
        ORDER BY dateLong
        ASC
        """
    )
    fun getUpcomingTasks(today: Long, hideCompleted: Boolean): Flow<List<ExtendedTask>>

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE dateLong > :today AND
        completed = 0
    """
    )
    fun countUpcomingTasks(today: Long): Flow<Int>


    @Query("DELETE FROM cross_ref WHERE taskId = :taskId")
    suspend fun deleteCrossRefByTaskId(taskId: Int?)

    @Query("SELECT * FROM cross_ref WHERE taskId = :taskId")
    suspend fun getCrossRefByTaskId(taskId: Int?): TaskCategoryCrossRef

    @Transaction
    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    suspend fun getTasksOfCategory(categoryId: Int): List<CategoryWithTasks>

    @Transaction
    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    fun getCategoriesOfTask(taskId: Int): Flow<TaskWithCategories?>

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT categoryId FROM category WHERE categoryName =:categoryName")
    suspend fun getCategoryIdByName(categoryName: String): Int

    @Query("SELECT MAX(taskId) FROM task_table")
    suspend fun getTaskMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCategoryCrossRef(crossRef: TaskCategoryCrossRef)

    @Update
    suspend fun updateTask(task: Task)

    @Update
    suspend fun updateCategory(category: Category)

    @Update
    suspend fun updateCrossRef(crossRef: TaskCategoryCrossRef)

    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()


    // ---------- Statistics ----------

    @Query("SELECT * FROM daystat WHERE year =:year AND month =:month")
    suspend fun getStatMonth(year: Int, month: Int): List<DayStat>

    @Query("SELECT * FROM daystat WHERE year =:year")
    suspend fun getStatYear(year: Int): List<DayStat>

    @Query("SELECT * FROM daystat WHERE year =:year AND month =:month AND day =:day")
    suspend fun getStatDay(year: Int, month: Int, day: Int): DayStat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayStat(dayStat: DayStat)

    @Update
    suspend fun updateDayStat(dayStat: DayStat)

    @Delete
    suspend fun deleteDayStat(dayStat: DayStat)
}