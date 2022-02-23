package start.up.tracker.mvvm.view_models

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.data.database.dao.ArticlesDao
import start.up.tracker.data.entities.Article
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articlesDao: ArticlesDao
) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>>
        get() = _articles

    init {
        loadArticlesFromDb()
    }

    private fun loadArticlesFromDb() = viewModelScope.launch {
        val articlesList = articlesDao.getArticlesList().asLiveData().value ?: listOf()
        _articles.postValue(articlesList)
    }
}
