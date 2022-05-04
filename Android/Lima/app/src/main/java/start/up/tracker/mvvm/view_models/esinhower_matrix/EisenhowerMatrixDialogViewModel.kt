package start.up.tracker.mvvm.view_models.esinhower_matrix

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class EisenhowerMatrixDialogViewModel @Inject constructor(
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    val itemId = state.getLiveData<Int>(StateHandleKeys.SELECTED_EISENHOWER_MATRIX_ID)

    fun onEisenhowerMatrixItemClick(selectedItemId: Int) {
        itemId.value = selectedItemId
    }
}
