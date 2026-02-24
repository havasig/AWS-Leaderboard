package hu.havasig.awsleaderboard.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.havasig.awsleaderboard.presentation.screens.auth.AuthScreen
import hu.havasig.awsleaderboard.presentation.screens.home.HomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Auth.route) {
                AuthScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onCertClick = { certId ->
                        navController.navigate(Screen.CertDetail.createRoute(certId))
                    }
                )
            }
            composable(Screen.CertDetail.route) { backStackEntry ->
                val certId = backStackEntry.arguments?.getString("certId") ?: return@composable
                // TODO: CertDetailScreen
            }
            composable(Screen.Leaderboard.route) {
                // TODO: LeaderboardScreen
            }
            composable(Screen.Profile.route) {
                // TODO: ProfileScreen
            }
            composable(Screen.UserProfile.route) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: return@composable
                // TODO: UserProfileScreen
            }
        }
    }
}