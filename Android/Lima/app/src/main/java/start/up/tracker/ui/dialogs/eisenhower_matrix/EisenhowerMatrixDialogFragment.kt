package start.up.tracker.ui.dialogs.eisenhower_matrix

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.BaseListDialogFragmentBinding
import start.up.tracker.mvvm.view_models.esinhower_matrix.EisenhowerMatrixDialogViewModel
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseBottomSheetDialogFragment
import start.up.tracker.ui.list.adapters.edit_task.dialogs.DialogChoiceAdapter
import start.up.tracker.ui.list.generators.eisenhower_matrix.EisenhowerMatrixDialogGenerator
import start.up.tracker.ui.list.view_holders.edit_task.dialogs.DialogChoiceViewHolder
import start.up.tracker.utils.screens.ExtraCodes

@AndroidEntryPoint
class EisenhowerMatrixDialogFragment :
    BaseBottomSheetDialogFragment(R.layout.base_list_dialog_fragment),
    DialogChoiceViewHolder.DialogChoiceClickListener {

    private val viewModel: EisenhowerMatrixDialogViewModel by viewModels()

    private var binding: BaseListDialogFragmentBinding? = null

    private lateinit var adapter: DialogChoiceAdapter
    private var listExtension: ListExtension? = null
    private val generator = EisenhowerMatrixDialogGenerator()

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
        viewModel.onEisenhowerMatrixItemClick(choiceId)

        setFragmentResult(
            requestKey = ExtraCodes.EISENHOWER_MATRIX_REQUEST,
            result = bundleOf(ExtraCodes.EISENHOWER_MATRIX_REQUEST to choiceId)
        )

        findNavController().popBackStack()
    }

    private fun showData(itemId: Int) {
        adapter.updateItems(generator.getEisenhowerPrioritiesListItems(itemId))
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
        viewModel.itemId.observe(viewLifecycleOwner) { id ->
            showData(id)
        }
    }
}
