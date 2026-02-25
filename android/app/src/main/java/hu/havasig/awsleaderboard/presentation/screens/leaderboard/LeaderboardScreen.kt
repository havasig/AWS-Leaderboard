package hu.havasig.awsleaderboard.presentation.screens.leaderboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.havasig.awsleaderboard.annotation.AwsPreviews
import hu.havasig.awsleaderboard.data.model.LeaderboardEntry
import hu.havasig.awsleaderboard.ui.theme.AWSLeaderboardTheme
import hu.havasig.awsleaderboard.ui.theme.AwsOrange
import hu.havasig.awsleaderboard.ui.theme.AwsOrangeAccent

@Composable
fun LeaderboardScreen(
    onUserClick: (String) -> Unit,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LeaderboardContent(
        uiState = uiState,
        onUserClick = onUserClick
    )
}

@Composable
fun LeaderboardContent(
    uiState: LeaderboardUiState,
    onUserClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Orange header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AwsOrange)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AwsOrangeAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = AwsOrange,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Leaderboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Top AWS Certified Professionals",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }

        // Table header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("RANK", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(
                "USER",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
            Text("CERTS", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        }

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AwsOrange)
                }
            }

            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error, color = Color.Red)
                }
            }

            else -> {
                LazyColumn {
                    itemsIndexed(uiState.entries) { index, entry ->
                        LeaderboardRow(
                            rank = index + 1,
                            entry = entry,
                            onClick = { onUserClick(entry.username) }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(
    rank: Int,
    entry: LeaderboardEntry,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$rank",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.size(32.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = entry.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${entry.materialsCompleted} materials completed",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        if (entry.certsEarned > 0) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(entry.certsEarned) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = AwsOrangeAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else {
            Text(
                text = "No certs yet",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@AwsPreviews
@Composable
fun LeaderboardScreenPreview(
    @PreviewParameter(LeaderboardUiStatePreviewParamProvider::class) leaderboardUiStateParameter: LeaderboardUiState,
) {
    AWSLeaderboardTheme {
        Surface {
            LeaderboardContent(
                uiState = leaderboardUiStateParameter,
                onUserClick = {}

            )
        }
    }
}

class LeaderboardUiStatePreviewParamProvider : PreviewParameterProvider<LeaderboardUiState> {
    override val values: Sequence<LeaderboardUiState> = sequenceOf(
        LeaderboardUiState(),
        LeaderboardUiState(isLoading = true),
        LeaderboardUiState(error = "Something went wrong"),
        LeaderboardUiState(
            entries = listOf(
                LeaderboardEntry("AWSExpert", 3, 45),
                LeaderboardEntry("CloudArchitect", 2, 13),
                LeaderboardEntry("DevOpsQueen", 1, 10),
                LeaderboardEntry("DataNerd", 1, 6),
                LeaderboardEntry("NewbieCloud", 0, 2)
            )
        )
    )
}
