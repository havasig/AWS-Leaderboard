package hu.havasig.awsleaderboard.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.havasig.awsleaderboard.data.model.CertProgress
import hu.havasig.awsleaderboard.data.repository.CertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val certProgress: List<CertProgress> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val certRepository: CertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val progress = certRepository.getProgress()
                _uiState.value = _uiState.value.copy(isLoading = false, certProgress = progress)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load progress")
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load progress")
            }
        }
    }

    fun completeMaterial(certId: String, materialId: String) {
        viewModelScope.launch {
            try {
                certRepository.completeMaterial(certId, materialId)
                loadProgress()
            } catch (e: Exception) {
                Timber.e(e, "Failed to complete material")
            }
        }
    }
}
