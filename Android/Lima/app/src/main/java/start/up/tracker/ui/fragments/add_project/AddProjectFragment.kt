package start.up.tracker.ui.fragments.add_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.databinding.AddProjectFragmentBinding
import start.up.tracker.entities.Project
import start.up.tracker.mvvm.view_models.add_project.AddProjectViewModel
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.add_project.ColorData
import start.up.tracker.ui.data.entities.add_project.ColorsData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseBottomSheetDialogFragment
import start.up.tracker.ui.list.adapters.add_project.AddProjectAdapter
import start.up.tracker.ui.list.generators.add_project.AddProjectGenerator
import start.up.tracker.ui.list.view_holders.add_project.AddProjectActionsViewHolder
import start.up.tracker.ui.list.view_holders.add_project.ColorViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView

@AndroidEntryPoint
class AddProjectFragment :
    BaseBottomSheetDialogFragment(),
    BaseInputView.TextInputListener,
    ColorViewHolder.ColorClickListener,
    AddProjectActionsViewHolder.AddProjectActionClickListener {

    private val viewModel: AddProjectViewModel by viewModels()

    private var binding: AddProjectFragmentBinding? = null

    private lateinit var adapter: AddProjectAdapter
    private var listExtension: ListExtension? = null
    private val generator = AddProjectGenerator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_project_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddProjectFragmentBinding.bind(view)

        setupAdapter()
        setupObservers()
        setupEventsListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onTextInputDataChange(listItem: ListItem) {
        viewModel.onProjectTitleChanged(listItem.data as String)
    }

    override fun onFocusLose(listItem: ListItem) {
        hideKeyboard()
        binding?.addProjectList?.requestFocus()
    }

    override fun onFocusGain(listItem: ListItem) {
        /*
         * Код добавлен, так как не всегда при захвате фокуса показывается клавиатура.
         * Например, когда используется функция copy-paste
         */
        showKeyboard()
    }

    override fun onClearClick(listItem: ListItem) {
        viewModel.onTitleClearClick()
    }

    override fun onDoneClick(item: ListItem) {
        hideKeyboard()
    }

    override fun onColorClick(colorData: ColorData) {
        viewModel.onColorClick(colorData)
    }

    override fun onDoneButtonClick() {
        viewModel.onDoneButtonClicked()
    }

    override fun onBackButtonClick() {
        viewModel.onBackButtonClick()
    }

    private fun setupObservers() {
        viewModel.projectTitle.observe(viewLifecycleOwner) { project ->
            showTitleInput(project)
        }

        viewModel.projectActions.observe(viewLifecycleOwner) { isEnabled ->
            showActions(isEnabled)
        }

        viewModel.colorsCircles.observe(viewLifecycleOwner) { colors ->
            showColors(colors)
        }
    }

    private fun showColors(colorsData: ColorsData) {
        val listItem: ListItem = generator.createColorsDataListItem(colorsData)

        if (binding?.addProjectList?.isComputingLayout == false) {
            adapter.setColorsDataItem(listItem)
            return
        }

        binding?.addProjectList?.post {
            adapter.setColorsDataItem(listItem)
        }
    }

    private fun showTitleInput(project: Project) {
        val listItem: ListItem = generator.createTitleListItem(project)

        if (binding?.addProjectList?.isComputingLayout == false) {
            adapter.setTitleItem(listItem)
            return
        }

        binding?.addProjectList?.post {
            adapter.setTitleItem(listItem)
        }
    }

    private fun showActions(isEnabled: Boolean) {
        val listItem: ListItem = generator.createActionsListItem(isEnabled)

        if (binding?.addProjectList?.isComputingLayout == false) {
            adapter.setActionsItem(listItem)
            return
        }

        binding?.addProjectList?.post {
            adapter.setActionsItem(listItem)
        }
    }

    private fun setupEventsListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addCategoryEvent.collect { event ->
                when (event) {
                    is AddProjectViewModel.AddProjectEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = AddProjectAdapter(
            layoutInflater = layoutInflater,
            textInputListener = this,
            addProjectActionsClickListener = this,
            colorClickListener = this
        )

        listExtension = ListExtension(binding?.addProjectList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }
}
