package start.up.tracker.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Category

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories_table")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT categoryId FROM categories_table WHERE categoryName =:categoryName")
    suspend fun getCategoryIdByName(categoryName: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}
