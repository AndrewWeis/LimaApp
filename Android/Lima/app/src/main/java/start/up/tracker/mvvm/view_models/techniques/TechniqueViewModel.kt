package start.up.tracker.mvvm.view_models.techniques

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.database.dao.TechniquesDao
import start.up.tracker.entities.Technique
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val techniquesDao: TechniquesDao,
    private val activeAnalytics: ActiveAnalytics,
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    val technique = state.getLiveData<Technique>(StateHandleKeys.TECHNIQUE)

    private val _analyticsMessageDialog: MutableLiveData<AnalyticsMessage> = MutableLiveData()
    val analyticsMessageDialog: LiveData<AnalyticsMessage> get() = _analyticsMessageDialog

    fun onTechniqueEnableClick() = viewModelScope.launch {
        val analyticsMessage = activeAnalytics.checkPrinciplesCompatibility(technique.value!!.id)

        analyticsMessage?.let {
            _analyticsMessageDialog.postValue(it)
        } ?: updateTechnique(true)
    }

    fun onTechniqueDisableClick() = viewModelScope.launch {
        updateTechnique(false)
    }

    private suspend fun updateTechnique(isEnabled: Boolean) {
        techniquesDao.updateTechnique(technique.value!!.copy(isEnabled = isEnabled))
        technique.postValue(technique.value!!.copy(isEnabled = isEnabled))
    }
}
