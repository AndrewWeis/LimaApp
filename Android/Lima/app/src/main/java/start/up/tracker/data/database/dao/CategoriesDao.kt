package start.up.tracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.entities.Category

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT categoryId FROM category WHERE categoryName =:categoryName")
    suspend fun getCategoryIdByName(categoryName: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}
