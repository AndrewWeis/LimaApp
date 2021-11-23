package start.up.tracker.ui.addcategory

import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import petrov.kristiyan.colorpicker.ColorPicker
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAddCategoryBinding
import start.up.tracker.utils.exhaustive

@AndroidEntryPoint
class AddCategoryFragment : Fragment(R.layout.fragment_add_category) {

    private val viewModel: AddCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddCategoryBinding.bind(view)

        var currentColor: Int

        binding.apply {
            editTextCategoryLabel.setText(viewModel.categoryName)

            editTextCategoryLabel.addTextChangedListener {
                viewModel.categoryName = it.toString()
            }

            fabSaveCategory.setOnClickListener {
                viewModel.onSaveClick()
            }

            colorPickerBtn.setOnClickListener {
                val colorPicker = ColorPicker(activity)
                colorPicker.setOnFastChooseColorListener(object: ColorPicker.OnFastChooseColorListener {
                    override fun setOnFastChooseColorListener(position: Int, color: Int) {
                        currentColor = color
                        colorPickerBtn.background.setTint(color)
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
        }


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addCategoryEvent.collect { event ->
                when(event) {
                    is AddCategoryViewModel.AddCategoryEvent.NavigateBackWithResult -> {
                        binding.editTextCategoryLabel.clearFocus()
                        setFragmentResult("add_request", bundleOf("add_request" to event.result))
                        findNavController().popBackStack()
                    }
                    is AddCategoryViewModel.AddCategoryEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}