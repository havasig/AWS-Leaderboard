package hu.havasig.awsleaderboard.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hu.havasig.awsleaderboard.presentation.screens.auth.AuthScreen
import hu.havasig.awsleaderboard.presentation.screens.certdetail.CertDetailScreen
import hu.havasig.awsleaderboard.presentation.screens.home.HomeScreen
import hu.havasig.awsleaderboard.presentation.screens.leaderboard.LeaderboardScreen
import hu.havasig.awsleaderboard.presentation.screens.profile.ProfileScreen
import hu.havasig.awsleaderboard.presentation.screens.profile.UserProfileScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Leaderboard.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = { if (showBottomBar) {
            BottomNavBar(navController = navController)
        } }
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
                CertDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.UserProfile.route) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: return@composable
                UserProfileScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(
                    onUserClick = { username ->
                        navController.navigate(Screen.UserProfile.createRoute(username))
                    }
                )
            }
        }
    }
}
