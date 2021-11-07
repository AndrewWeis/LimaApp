package start.up.tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.db.models.Category
import start.up.tracker.data.db.relations.CategoryWithTasks
import start.up.tracker.data.db.relations.TaskCategoryCrossRef
import start.up.tracker.data.db.relations.TaskWithCategories

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

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY important DESC, taskName")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM category WHERE categoryName = :categoryName")
    fun getTasksOfCategory(categoryName: String): LiveData<List<CategoryWithTasks>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE taskName = :taskName")
    fun getCategoriesOfTask(taskName: String): LiveData<List<TaskWithCategories>>

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

    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()
}