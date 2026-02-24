package hu.havasig.awsleaderboard.data.api

import hu.havasig.awsleaderboard.data.model.CertProgress
import hu.havasig.awsleaderboard.data.model.LeaderboardEntry
import hu.havasig.awsleaderboard.data.model.LoginRequest
import hu.havasig.awsleaderboard.data.model.LoginResponse
import hu.havasig.awsleaderboard.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): LoginResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("certs/progress")
    suspend fun getProgress(): List<CertProgress>

    @GET("users/{username}/progress")
    suspend fun getUserProgress(@Path("username") username: String): List<CertProgress>

    @POST("certs/{certId}/materials/{materialId}/complete")
    suspend fun completeMaterial(
        @Path("certId") certId: String,
        @Path("materialId") materialId: String
    ): Map<String, Boolean>

    @GET("leaderboard")
    suspend fun getLeaderboard(): List<LeaderboardEntry>
}
