package start.up.tracker.database.dao

import androidx.room.*
import start.up.tracker.entities.Article

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticles(vararg article: Article)

    @Update
    suspend fun updateArticle(article: Article)

    @Query("SELECT * FROM articles_table")
    suspend fun getArticlesList(): List<Article>
}