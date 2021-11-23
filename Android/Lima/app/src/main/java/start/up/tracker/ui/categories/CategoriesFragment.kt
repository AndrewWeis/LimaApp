package start.up.tracker.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.data.db.models.Category
import start.up.tracker.databinding.FragmentCategoriesBinding
import start.up.tracker.utils.exhaustive

@AndroidEntryPoint
class CategoriesFragment: Fragment(R.layout.fragment_categories), CategoriesAdapter.OnItemClickListener{

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCategoriesBinding.bind(view)

        val categoryAdapter = CategoriesAdapter(this)

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

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.categoryEvent.collect { event ->
                when(event) {
                    is CategoriesViewModel.CategoryEvent.NavigateToCategoryInside -> {
                        val action = CategoriesFragmentDirections.actionCategoryFragmentToCategoryInsideFragment(event.category, event.category.categoryName)
                        findNavController().navigate(action)
                    }
                    CategoriesViewModel.CategoryEvent.NavigateToAddCategoryScreen -> {
                        val action = CategoriesFragmentDirections.actionCategoryFragmentToAddCategoryFragment()
                        findNavController().navigate(action)
                    }
                    is CategoriesViewModel.CategoryEvent.ShowCategorySavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }

        binding.cardViewInbox.setOnClickListener {
            viewModel.onCategoryInboxSelected(Category("Inbox"))
        }

        binding.addCategoryFAB.setOnClickListener {
            viewModel.onAddNewCategoryClick()
        }

        setFragmentResultListener("add_request") { _, _ ->
            viewModel.showCategorySavedConfirmationMessage()
        }

        viewModel.updateNumberOfTasks()

        viewModel.getInboxTasksCount.observe(viewLifecycleOwner) {
            binding.tvInboxNum.text = it.toString()
        }
    }

    override fun onItemClick(category: Category) {
        viewModel.onCategorySelected(category)
    }
}