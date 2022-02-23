package start.up.tracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.entities.Article

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticles(vararg article: Article)

    @Update
    suspend fun updateArticle(article: Article)

    @Query("SELECT * FROM articles_table")
    fun getArticlesList(): Flow<List<Article>>
}
