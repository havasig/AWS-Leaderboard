package hu.havasig.awsleaderboard.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.havasig.awsleaderboard.annotation.AwsPreviews
import hu.havasig.awsleaderboard.data.model.CertProgress
import hu.havasig.awsleaderboard.ui.theme.AWSLeaderboardTheme
import hu.havasig.awsleaderboard.ui.theme.AwsOrange
import hu.havasig.awsleaderboard.ui.theme.AwsOrangeAccent
import hu.havasig.awsleaderboard.ui.theme.DarkOnSurface

private const val MAXIMUM_PERCENTAGE = 100

@Composable
fun HomeScreen(
    onCertClick: (String) -> Unit,
    username: String,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onCertClick = onCertClick,
        username = username,
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onCertClick: (String) -> Unit,
    username: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Learning",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pick up where you left off",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            // Avatar
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = AwsOrange
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = username.take(2).uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AwsOrange)
                }
            }

            uiState.error != null -> {
                Text(text = uiState.error, color = Color.Red)
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.certProgress) { cert ->
                        CertCard(cert = cert, onClick = { onCertClick(cert.certId) })
                    }
                }
            }
        }
    }
}

@Composable
fun CertCard(
    cert: CertProgress,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cert.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (cert.certified) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = "Certified",
                        tint = AwsOrangeAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val percentage =
                (cert.completedMaterials.toFloat() / cert.totalMaterials.toFloat() * MAXIMUM_PERCENTAGE).toInt()

            Text(
                text = "$percentage% Completed",
                fontSize = 12.sp,
                color = if (cert.certified) AwsOrange else Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { cert.completedMaterials.toFloat() / cert.totalMaterials.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = AwsOrange,
                trackColor = DarkOnSurface,
            )

            if (cert.certified) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = AwsOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " Certified",
                        fontSize = 12.sp,
                        color = AwsOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@AwsPreviews
@Composable
fun HomeScreenPreview(
    @PreviewParameter(HomeUiStatePreviewParamProvider::class) homeUiStateParameter: HomeUiState,
) {
    AWSLeaderboardTheme {
        Surface {
            HomeContent(
                uiState = homeUiStateParameter,
                onCertClick = {},
                username = "AL"
            )
        }
    }
}

class HomeUiStatePreviewParamProvider : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState> = sequenceOf(
        HomeUiState(isLoading = true),
        HomeUiState(error = "Something went wrong"),
        HomeUiState(),
        HomeUiState(
            certProgress = listOf(
                CertProgress(
                    certId = "sa-associate",
                    title = "SA Associate",
                    shortTitle = "SAA",
                    completedMaterials = 3,
                    totalMaterials = 3,
                    certified = true,
                    materials = emptyList()
                ),
                CertProgress(
                    certId = "sa-pro",
                    title = "SA Professional",
                    shortTitle = "SAP",
                    completedMaterials = 0,
                    totalMaterials = 3,
                    certified = false,
                    materials = emptyList()
                ),
                CertProgress(
                    certId = "ml-specialty",
                    title = "ML Specialty",
                    shortTitle = "MLS",
                    completedMaterials = 0,
                    totalMaterials = 3,
                    certified = false,
                    materials = emptyList()
                ),
                CertProgress(
                    certId = "genai-pro",
                    title = "GenAI Practitioner",
                    shortTitle = "GenAI",
                    completedMaterials = 2,
                    totalMaterials = 3,
                    certified = false,
                    materials = emptyList()
                )
            )
        ),
    )
}

