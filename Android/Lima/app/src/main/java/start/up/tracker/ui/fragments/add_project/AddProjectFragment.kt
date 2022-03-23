package start.up.tracker.ui.fragments.add_project

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import petrov.kristiyan.colorpicker.ColorPicker
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAddProjectBinding
import start.up.tracker.mvvm.view_models.add_project.AddProjectViewModel
import start.up.tracker.utils.exhaustive

@AndroidEntryPoint
class AddProjectFragment : Fragment(R.layout.fragment_add_project) {

    private val viewModel: AddProjectViewModel by viewModels()

    private var binding: FragmentAddProjectBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddProjectBinding.bind(view)

        initData()
        initListeners()
        initProjectsEventListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initProjectsEventListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addCategoryEvent.collect { event ->
                when (event) {
                    is AddProjectViewModel.AddCategoryEvent.NavigateBack -> {
                        binding?.editTextCategoryLabel?.clearFocus()
                        findNavController().popBackStack()
                    }
                    is AddProjectViewModel.AddCategoryEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }

    private fun initListeners() {
        binding?.editTextCategoryLabel?.addTextChangedListener {
            viewModel.projectName = it.toString()
        }

        binding?.fabSaveCategory?.setOnClickListener {
            viewModel.onSaveClick()
        }

        binding?.colorPickerBtn?.setOnClickListener {
            initColorPicker()
        }
    }

    private fun initColorPicker() {
        val colorPicker = ColorPicker(activity)

        colorPicker.setOnFastChooseColorListener(object : ColorPicker.OnFastChooseColorListener {
            override fun setOnFastChooseColorListener(position: Int, color: Int) {
                viewModel.color = color
                binding?.colorPickerBtn?.background?.setTint(color)
            }

            override fun onCancel() {
                colorPicker.dismissDialog()
            }
        })
            .disableDefaultButtons(true)
            .setColumns(5)
            .setRoundColorButton(true)
            .show()
    }

    private fun initData() {
        binding?.editTextCategoryLabel?.setText(viewModel.projectName)
    }
}
