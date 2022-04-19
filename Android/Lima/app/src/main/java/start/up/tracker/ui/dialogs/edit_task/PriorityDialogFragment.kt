package start.up.tracker.ui.dialogs.edit_task

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.database.TechniquesIds
import start.up.tracker.databinding.BaseListDialogFragmentBinding
import start.up.tracker.mvvm.view_models.edit_task.dialogs.PriorityViewModel
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseBottomSheetDialogFragment
import start.up.tracker.ui.list.adapters.edit_task.dialogs.DialogChoiceAdapter
import start.up.tracker.ui.list.generators.edit_task.dialogs.PriorityGenerator
import start.up.tracker.ui.list.view_holders.edit_task.dialogs.DialogChoiceViewHolder
import start.up.tracker.utils.screens.ExtraCodes

@AndroidEntryPoint
class PriorityDialogFragment :
    BaseBottomSheetDialogFragment(R.layout.base_list_dialog_fragment),
    DialogChoiceViewHolder.DialogChoiceClickListener {

    private val viewModel: PriorityViewModel by viewModels()

    private var binding: BaseListDialogFragmentBinding? = null

    private lateinit var adapter: DialogChoiceAdapter
    private var listExtension: ListExtension? = null
    private val generator = PriorityGenerator()

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

    override fun onChoiceClick(choiceId: Int) {
        viewModel.onPriorityClick(choiceId)

        setFragmentResult(
            requestKey = ExtraCodes.PRIORITY_REQUEST,
            result = bundleOf(ExtraCodes.PRIORITY_REQUEST to choiceId)
        )

        findNavController().popBackStack()
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
        adapter = DialogChoiceAdapter(
            layoutInflater = layoutInflater,
             dialogChoiceClickListener = this
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
