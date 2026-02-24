package hu.havasig.awsleaderboard.presentation.screens.profile

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

data class UserProfileUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val certProgress: List<CertProgress> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val certRepository: CertRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val username: String = checkNotNull(savedStateHandle["username"])

    private val _uiState = MutableStateFlow(UserProfileUiState(username = username))
    val uiState: StateFlow<UserProfileUiState> = _uiState

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val progress = certRepository.getUserProgress(username)
                _uiState.value = _uiState.value.copy(isLoading = false, certProgress = progress)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load user profile")
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load profile")
            }
        }
    }
}