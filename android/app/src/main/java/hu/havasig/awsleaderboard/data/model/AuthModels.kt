package hu.havasig.awsleaderboard.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
