package hu.havasig.awsleaderboard.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.havasig.awsleaderboard.data.model.LeaderboardEntry
import hu.havasig.awsleaderboard.data.repository.CertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class LeaderboardUiState(
    val isLoading: Boolean = false,
    val entries: List<LeaderboardEntry> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val certRepository: CertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val entries = certRepository.getLeaderboard()
                _uiState.value = _uiState.value.copy(isLoading = false, entries = entries)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load leaderboard")
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load leaderboard")
            }
        }
    }
}
