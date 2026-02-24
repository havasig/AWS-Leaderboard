package hu.havasig.awsleaderboard.presentation.screens.certdetail

import androidx.lifecycle.SavedStateHandle
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

data class CertDetailUiState(
    val isLoading: Boolean = false,
    val cert: CertProgress? = null,
    val error: String? = null
)

@HiltViewModel
class CertDetailViewModel @Inject constructor(
    private val certRepository: CertRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val certId: String = checkNotNull(savedStateHandle["certId"])

    private val _uiState = MutableStateFlow(CertDetailUiState())
    val uiState: StateFlow<CertDetailUiState> = _uiState

    init {
        loadCertDetail()
    }

    fun loadCertDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val progress = certRepository.getProgress()
                val cert = progress.find { it.certId == certId }
                _uiState.value = _uiState.value.copy(isLoading = false, cert = cert)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load cert detail")
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load cert")
            }
        }
    }

    fun completeMaterial(materialId: String) {
        viewModelScope.launch {
            try {
                certRepository.completeMaterial(certId, materialId)
                loadCertDetail()
            } catch (e: Exception) {
                Timber.e(e, "Failed to complete material")
            }
        }
    }
}