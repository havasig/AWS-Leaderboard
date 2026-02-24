package hu.havasig.awsleaderboard.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.havasig.awsleaderboard.data.model.CertProgress
import hu.havasig.awsleaderboard.data.repository.AuthRepository
import hu.havasig.awsleaderboard.data.repository.CertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val certProgress: List<CertProgress> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val certRepository: CertRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val progress = certRepository.getProgress()
                val token = authRepository.getToken().first()
                val username = extractUsernameFromToken(token)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    certProgress = progress,
                    username = username
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to load profile")
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load profile")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    private fun extractUsernameFromToken(token: String?): String {
        if (token == null) return ""
        return try {
            val payload = token.split(".")[1]
            val decoded = String(android.util.Base64.decode(payload, android.util.Base64.URL_SAFE))
            val json = org.json.JSONObject(decoded)
            json.getString("username")
        } catch (e: Exception) {
            ""
        }
    }
}