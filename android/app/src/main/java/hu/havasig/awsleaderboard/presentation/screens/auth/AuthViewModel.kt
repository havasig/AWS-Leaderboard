package hu.havasig.awsleaderboard.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.havasig.awsleaderboard.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                authRepository.login(username, password)
                _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true)
            } catch (e: Exception) {
                Timber.e(e, "Login failed")
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Invalid credentials")
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                authRepository.register(username, password)
                _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true)
            } catch (e: Exception) {
                Timber.e(e, "Registration failed")
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Registration failed")
            }
        }
    }
}
