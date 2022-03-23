package start.up.tracker.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Task

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
       JOIN projects_table ON task_table.projectId = projects_table.projectId
       WHERE projects_table.projectId = :categoryId AND
       task_table.parentTaskId == -1 AND
       (completed != :hideCompleted OR completed = 0) AND 
       task_table.taskTitle LIKE '%' || :searchQuery || '%' 
       ORDER BY priority 
       ASC, created"""
    )
    fun getTasksOfCategory(searchQuery: String, hideCompleted: Boolean, categoryId: Int): Flow<List<Task>>

    @Query(
        """
        SELECT COUNT(*) 
        FROM task_table 
        JOIN projects_table ON task_table.projectId = projects_table.projectId
        WHERE projects_table.projectId = :projectId AND
        parentTaskId == -1 AND
        completed == 0
    """
    )
    fun countTasksOfProject(projectId: Int): Flow<Int>

    @Query(" SELECT * FROM task_table WHERE parentTaskId = :id")
    fun getSubtasksByTaskId(id: Int): Flow<List<Task>>

    @Query(" SELECT * FROM task_table WHERE parentTaskId = :id")
    suspend fun getSubtasksToRestore(id: Int): List<Task>

    @Query("UPDATE task_table SET subtasksNumber = :number WHERE taskId = :taskId")
    suspend fun updateSubtasksNumber(number: Int, taskId: Int)

    @Query("UPDATE task_table SET completedSubtasksNumber = :number WHERE taskId = :taskId")
    suspend fun updateCompletedSubtasksNumber(number: Int, taskId: Int)

    @Query("SELECT MAX(taskId) FROM task_table")
    suspend fun getTaskMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtasks(subtasks: List<Task>)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()

    @Query("DELETE FROM task_table WHERE parentTaskId = :taskId")
    suspend fun deleteSubtasks(taskId: Int)
}
