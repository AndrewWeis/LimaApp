package start.up.tracker.mvvm.view_models.techniques

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.database.PreferencesManager
import javax.inject.Inject

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    val selectedTechniqueId = preferencesManager.selectedTechniqueId.asLiveData()

    fun onSelectTechniqueClick(id: Int) = viewModelScope.launch {
        preferencesManager.updateTechniqueId(id)
    }

    fun onCancelSelectTechniqueClick() = viewModelScope.launch {
        preferencesManager.updateTechniqueId(NO_ID)
    }

    companion object {
        const val NO_ID = -1
    }
}
