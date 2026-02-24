package hu.havasig.awsleaderboard.presentation.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object CertDetail : Screen("cert_detail/{certId}") {
        fun createRoute(certId: String) = "cert_detail/$certId"
    }
    object Leaderboard : Screen("leaderboard")
    object Profile : Screen("profile")
    object UserProfile : Screen("user_profile/{username}") {
        fun createRoute(username: String) = "user_profile/$username"
    }
}
