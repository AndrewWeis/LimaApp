package start.up.tracker.ui.fragments.categories

import android.os.Bundle
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
import start.up.tracker.databinding.FragmentCategoriesBinding
import start.up.tracker.entities.Category
import start.up.tracker.mvvm.view_models.categories.CategoriesViewModel
import start.up.tracker.ui.list.adapters.CategoriesAdapter
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CategoriesFragment :
    Fragment(R.layout.fragment_categories),
    CategoriesAdapter.OnItemClickListener {

    private val viewModel: CategoriesViewModel by viewModels()

    private var binding: FragmentCategoriesBinding? = null
    private lateinit var categoryAdapter: CategoriesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoriesBinding.bind(view)

        initAdapter()
        initListeners()
        initTaskEventListener()

        viewModel.updateNumberOfTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(category: Category) {
        viewModel.onCategorySelected(category)
    }

    private fun initTaskEventListener() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.categoryEvent.collect { event ->
            when (event) {
                is CategoriesViewModel.CategoryEvent.NavigateToCategoryInside -> {
                    val action = CategoriesFragmentDirections.actionCategoryToProjectTasks(
                        event.category.categoryId,
                        event.category.categoryName
                    )
                    findNavController().navigate(action)
                }
                CategoriesViewModel.CategoryEvent.NavigateToAddCategoryScreen -> {
                    val action = CategoriesFragmentDirections.actionCategoryToAddCategory()
                    findNavController().navigate(action)
                }
                is CategoriesViewModel.CategoryEvent.ShowCategorySavedConfirmationMessage -> {
                    Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                }
                CategoriesViewModel.CategoryEvent.NavigateToToday -> {
                    val formatter = SimpleDateFormat("dd MMM, EEEE")
                    val currentDate: String = formatter.format(Date())
                    val action = CategoriesFragmentDirections.actionCategoryToToday(currentDate)
                    findNavController().navigate(action)
                }
                CategoriesViewModel.CategoryEvent.NavigateToUpcoming -> {
                    val action = CategoriesFragmentDirections.actionCategoryToUpcoming()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun initListeners() {
        binding?.cardViewInbox?.setOnClickListener {
            viewModel.onCategoryInboxSelected(Category(categoryName = "Inbox", categoryId = 1))
        }

        binding?.addCategoryFAB?.setOnClickListener {
            viewModel.onAddNewCategoryClick()
        }

        binding?.cardViewToday?.setOnClickListener {
            viewModel.onTodaySelected()
        }

        binding?.cardViewUpcoming?.setOnClickListener {
            viewModel.onUpcomingSelected()
        }

        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
        }

        viewModel.getInboxTasksCount.observe(viewLifecycleOwner) {
            binding?.tvInboxNum?.text = it.toString()
        }

        viewModel.todayTasksCount.observe(viewLifecycleOwner) {
            binding?.tvTodayNum?.text = it.toString()
        }

        viewModel.upcomingTasksCount.observe(viewLifecycleOwner) {
            binding?.tvUpcomingNum?.text = it.toString()
        }

        setFragmentResultListener("add_request") { _, _ ->
            viewModel.showCategorySavedConfirmationMessage()
        }
    }

    private fun initAdapter() {
        categoryAdapter = CategoriesAdapter(this)

        binding?.categoryRV?.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}
