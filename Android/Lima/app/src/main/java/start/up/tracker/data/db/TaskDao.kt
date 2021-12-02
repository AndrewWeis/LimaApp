package start.up.tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.models.Category
import start.up.tracker.data.models.Task
import start.up.tracker.data.models.TodayTask
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

    fun getTasksOfCategory(query: String, sortOrder: SortOrder, hideCompleted: Boolean, categoryName: String): Flow<List<Task>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksOfCategorySortedByDateCreated(query, hideCompleted, categoryName)
            SortOrder.BY_NAME -> getTasksOfCategorySortedByName(query, hideCompleted, categoryName)
        }

    @Query("""
       SELECT * 
       FROM task_table 
       JOIN cross_ref ON task_table.taskName = cross_ref.taskName
       WHERE categoryName = :categoryName AND
       (completed != :hideCompleted OR completed = 0) AND 
       task_table.taskName LIKE '%' || :searchQuery || '%' 
       ORDER BY important 
       DESC, taskName
       """)
    fun getTasksOfCategorySortedByName(searchQuery: String, hideCompleted: Boolean, categoryName: String): Flow<List<Task>>

    @Query("""
       SELECT * 
       FROM task_table
       JOIN cross_ref ON task_table.taskName = cross_ref.taskName
       WHERE categoryName = :categoryName AND
       (completed != :hideCompleted OR completed = 0) AND 
       task_table.taskName LIKE '%' || :searchQuery || '%' 
       ORDER BY important 
       DESC, created""")
    fun getTasksOfCategorySortedByDateCreated(searchQuery: String, hideCompleted: Boolean, categoryName: String): Flow<List<Task>>

    @Query("""
        SELECT COUNT(*) 
        FROM task_table 
        JOIN cross_ref ON task_table.taskName = cross_ref.taskName
        WHERE categoryName = :categoryName AND
        (completed != :hideCompleted OR completed = 0)
    """)
    suspend fun countTasksOfCategory(categoryName: String, hideCompleted: Boolean): Int

    @Query("""
        SELECT COUNT(*)
        FROM task_table 
        JOIN cross_ref ON task_table.taskName = cross_ref.taskName
        WHERE categoryName = 'Inbox' AND
        completed = 0
    """)
    fun countTasksOfInbox(): Flow<Int>

    @Query("""
        SELECT 
	        task_table.id, task_table.taskName, task_table.important, task_table.completed, task_table.created, task_table.date,
	        Category.categoryName, Category.color, Category.tasksInside
        FROM cross_ref
        JOIN task_table ON task_table.taskName = cross_ref.taskName
        JOIN Category ON Category.categoryName = cross_ref.categoryName
        WHERE task_table.date = :today AND
       (completed != :hideCompleted OR completed = 0)
    """)
    fun getTodayTasks(today: String, hideCompleted: Boolean): Flow<List<TodayTask>>

    @Query("DELETE FROM cross_ref WHERE taskName = :taskName")
    suspend fun deleteCrossRefByTaskName(taskName: String?)

    @Query("SELECT * FROM cross_ref WHERE taskName = :taskName")
    suspend fun getCrossRefByTaskName(taskName: String?): TaskCategoryCrossRef

    @Transaction
    @Query("SELECT * FROM category WHERE categoryName = :categoryName")
    suspend fun getTasksOfCategory(categoryName: String): List<CategoryWithTasks>

    @Transaction
    @Query("SELECT * FROM task_table WHERE taskName = :taskName")
    fun getCategoriesOfTask(taskName: String): Flow<TaskWithCategories?>

    @Query("SELECT * FROM category")
    fun getCategories() : Flow<List<Category>>

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
}