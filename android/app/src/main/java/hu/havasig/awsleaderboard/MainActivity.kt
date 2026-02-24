package hu.havasig.awsleaderboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import hu.havasig.awsleaderboard.presentation.navigation.NavGraph
import hu.havasig.awsleaderboard.ui.theme.AWSLeaderboardTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AWSLeaderboardTheme {
                NavGraph()
            }
        }
    }
}
