package start.up.tracker.ui.fragments.edit_task.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.database.TechniquesIds
import start.up.tracker.databinding.BaseListDialogFragmentBinding
import start.up.tracker.mvvm.view_models.edit_task.dialogs.PriorityViewModel
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.edit_task.dialogs.PriorityAdapter
import start.up.tracker.ui.list.generators.edit_task.dialogs.PriorityGenerator
import start.up.tracker.ui.list.view_holders.edit_task.dialogs.PriorityViewHolder

@AndroidEntryPoint
class PriorityDialogFragment :
    BottomSheetDialogFragment(),
    PriorityViewHolder.PriorityClickListener {

    private val viewModel: PriorityViewModel by viewModels()

    private var binding: BaseListDialogFragmentBinding? = null

    private lateinit var adapter: PriorityAdapter
    private var listExtension: ListExtension? = null
    private val generator = PriorityGenerator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_list_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BaseListDialogFragmentBinding.bind(view)

        setupAdapter()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onPriorityClick(priorityId: Int) {
        viewModel.onPriorityClick(priorityId)
    }

    private fun showData(priorityId: Int) {
        when (viewModel.principleId) {
            TechniquesIds.EISENHOWER_MATRIX -> showEisenhowerPrinciples(priorityId)
            TechniquesIds.POMODORO -> showPomodoroPrinciples(priorityId)
            else -> showDefaultPrinciples(priorityId)
        }
    }

    private fun showPomodoroPrinciples(priorityId: Int) {

    }

    private fun showEisenhowerPrinciples(priorityId: Int) {
        adapter.updateItems(generator.getEisenhowerPrioritiesListItems(priorityId))
    }

    private fun showDefaultPrinciples(priorityId: Int) {
        adapter.updateItems(generator.getDefaultPrioritiesListItems(priorityId))
    }

    private fun setupAdapter() {
        adapter = PriorityAdapter(
            layoutInflater = layoutInflater,
            priorityClickListener = this
        )

        listExtension = ListExtension(binding?.dialogList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun setupObservers() {
        viewModel.priorityId.observe(viewLifecycleOwner) { id ->
            showData(id)
        }
    }
}
