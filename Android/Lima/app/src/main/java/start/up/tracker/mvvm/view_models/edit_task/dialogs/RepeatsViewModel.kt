package start.up.tracker.mvvm.view_models.edit_task.dialogs

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class RepeatsViewModel @Inject constructor(
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    val repeatsId = state.getLiveData<Int>(StateHandleKeys.SELECTED_REPEATS_ID)

    fun onRepeatsClick(selectedRepeatsId: Int) {
        repeatsId.postValue(selectedRepeatsId)
    }
}
