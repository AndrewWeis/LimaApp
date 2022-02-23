package start.up.tracker.data.database

import start.up.tracker.data.entities.Article

object ArticleStorage {

    fun getArticles(): List<Article> {
        val articles: MutableList<Article> = mutableListOf()

        repeat(10) { index ->
            articles.add(Article(index, "Title: $index", "Body: $index", "$index min"))
        }

        return articles.toList()
    }
}
