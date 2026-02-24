package hu.havasig.awsleaderboard.data.repository

import hu.havasig.awsleaderboard.data.api.ApiService
import hu.havasig.awsleaderboard.data.model.LoginRequest
import hu.havasig.awsleaderboard.data.model.RegisterRequest
import hu.havasig.awsleaderboard.domain.datastore.TokenDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenDataStore: TokenDataStore
) {
    suspend fun register(username: String, password: String) {
        val response = apiService.register(RegisterRequest(username, password))
        tokenDataStore.saveToken(response.token)
    }

    suspend fun login(username: String, password: String) {
        val response = apiService.login(LoginRequest(username, password))
        tokenDataStore.saveToken(response.token)
    }

    suspend fun logout() {
        tokenDataStore.clearToken()
    }

    fun getToken() = tokenDataStore.token
}
