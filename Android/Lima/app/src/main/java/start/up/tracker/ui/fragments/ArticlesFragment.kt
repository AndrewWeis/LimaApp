package start.up.tracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.databinding.FragmentArticlesBinding
import start.up.tracker.ui.list.adapters.ArticlesAdapter

@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArticlesBinding.bind(view)
        val articlesAdapter = ArticlesAdapter()

    }
}
