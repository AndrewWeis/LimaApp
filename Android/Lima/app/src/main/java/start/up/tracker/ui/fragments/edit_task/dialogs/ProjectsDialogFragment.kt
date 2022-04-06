package start.up.tracker.ui.fragments.edit_task.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.BaseListDialogFragmentBinding
import start.up.tracker.mvvm.view_models.edit_task.dialogs.ProjectsDialogViewModel
import start.up.tracker.ui.data.entities.edit_task.ProjectData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseBottomSheetDialogFragment
import start.up.tracker.ui.list.adapters.edit_task.dialogs.DialogChoiceAdapter
import start.up.tracker.ui.list.generators.edit_task.dialogs.ProjectsDialogGenerator
import start.up.tracker.ui.list.view_holders.edit_task.dialogs.DialogChoiceViewHolder
import start.up.tracker.utils.screens.ExtraCodes

@AndroidEntryPoint
class ProjectsDialogFragment :
    BaseBottomSheetDialogFragment(R.layout.base_list_dialog_fragment),
    DialogChoiceViewHolder.DialogChoiceClickListener {

    private val viewModel: ProjectsDialogViewModel by viewModels()

    private var binding: BaseListDialogFragmentBinding? = null

    private lateinit var adapter: DialogChoiceAdapter
    private var listExtension: ListExtension? = null
    private val generator = ProjectsDialogGenerator()

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
        viewModel.onProjectClick(choiceId)

        setFragmentResult(
            requestKey = ExtraCodes.PROJECT_REQUEST,
            result = bundleOf(ExtraCodes.PROJECT_REQUEST to choiceId)
        )

        findNavController().popBackStack()
    }

    private fun showData(projects: List<ProjectData>) {
        adapter.updateItems(generator.getProjectsListItems(projects))
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
        viewModel.projects.observe(viewLifecycleOwner) { projects ->
            showData(projects)
        }
    }
}
