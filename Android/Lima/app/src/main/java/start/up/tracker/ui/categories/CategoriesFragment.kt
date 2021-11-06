package start.up.tracker.ui.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentCategoriesBinding

@AndroidEntryPoint
class CategoriesFragment: Fragment(R.layout.fragment_categories) {

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCategoriesBinding.bind(view)

        val categoryAdapter = CategoriesAdapter(/*this*/)

        binding.apply {
            categoryRV.apply {
                adapter = categoryAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
        }

    }
}