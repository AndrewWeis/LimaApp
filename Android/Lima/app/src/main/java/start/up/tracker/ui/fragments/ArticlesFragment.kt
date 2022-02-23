package start.up.tracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.data.entities.Article
import start.up.tracker.databinding.FragmentArticlesBinding
import start.up.tracker.mvvm.view_models.ArticlesViewModel
import start.up.tracker.ui.list.adapters.ArticlesAdapter

@AndroidEntryPoint
class ArticlesFragment :
    Fragment(),
    ArticlesAdapter.OnArticleClickListener {

    private val viewModel: ArticlesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArticlesBinding.bind(view)
        val articlesAdapter = ArticlesAdapter(this)

        binding.articlesList.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.articles.observe(viewLifecycleOwner) {
            articlesAdapter.submitList(it)
        }
    }

    override fun onArticleClick(article: Article) {
        // navigate to article
    }
}
