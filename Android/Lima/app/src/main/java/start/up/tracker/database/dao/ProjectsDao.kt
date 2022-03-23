package start.up.tracker.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Project

@Dao
interface ProjectsDao {

    @Query("SELECT * FROM projects_table")
    fun getProjects(): Flow<List<Project>>

    @Query("SELECT projectId FROM projects_table WHERE projectTitle =:projectName")
    suspend fun getProjectIdByName(projectName: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)
}
