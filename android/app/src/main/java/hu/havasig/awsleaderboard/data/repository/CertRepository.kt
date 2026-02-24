package hu.havasig.awsleaderboard.data.repository

import hu.havasig.awsleaderboard.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProgress() = apiService.getProgress()

    suspend fun getUserProgress(username: String) = apiService.getUserProgress(username)

    suspend fun completeMaterial(certId: String, materialId: String) =
        apiService.completeMaterial(certId, materialId)

    suspend fun getLeaderboard() = apiService.getLeaderboard()
}
