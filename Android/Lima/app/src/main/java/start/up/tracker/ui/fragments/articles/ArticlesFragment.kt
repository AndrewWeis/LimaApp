package start.up.tracker.ui.fragments.articles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.entities.Article
import start.up.tracker.databinding.FragmentArticlesBinding
import start.up.tracker.mvvm.view_models.articles.ArticlesViewModel
import start.up.tracker.ui.list.adapters.ArticlesAdapter

@AndroidEntryPoint
class ArticlesFragment :
    Fragment(R.layout.fragment_articles),
    ArticlesAdapter.OnArticleClickListener {

    private val viewModel: ArticlesViewModel by viewModels()

    private lateinit var articlesAdapter: ArticlesAdapter
    private var binding: FragmentArticlesBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticlesBinding.bind(view)

        initAdapter()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onArticleClick(article: Article) {
        // navigate to article
    }

    private fun initObservers() {
        viewModel.articles.observe(viewLifecycleOwner) {
            articlesAdapter.submitList(it)
        }
    }

    private fun initAdapter() {
        articlesAdapter = ArticlesAdapter(this)

        binding?.articlesList?.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}
