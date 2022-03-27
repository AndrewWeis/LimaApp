package start.up.tracker.mvvm.view_models.techniques

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.database.dao.TechniquesDao
import start.up.tracker.entities.Technique
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val techniquesDao: TechniquesDao,
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    val technique = state.getLiveData<Technique>(StateHandleKeys.TECHNIQUE)

    fun onSelectTechniqueClick(isEnabled: Boolean) = viewModelScope.launch {
        techniquesDao.updateTechnique(technique.value!!.copy(isEnabled = isEnabled))
        technique.postValue(technique.value!!.copy(isEnabled = isEnabled))
    }
}
