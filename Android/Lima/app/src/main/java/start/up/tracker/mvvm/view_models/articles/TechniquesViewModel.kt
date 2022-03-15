package start.up.tracker.mvvm.view_models.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.database.dao.TechniquesDao
import javax.inject.Inject

@HiltViewModel
class TechniquesViewModel @Inject constructor(
    techniquesDao: TechniquesDao
) : ViewModel() {

    private val techniquesFlow = techniquesDao.getTechniques()
    val techniques = techniquesFlow.asLiveData()
}
