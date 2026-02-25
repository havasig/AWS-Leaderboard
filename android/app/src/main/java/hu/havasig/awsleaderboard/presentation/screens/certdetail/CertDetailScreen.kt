package hu.havasig.awsleaderboard.presentation.screens.certdetail

import android.content.Intent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.havasig.awsleaderboard.annotation.AwsPreviews
import hu.havasig.awsleaderboard.data.model.CertProgress
import hu.havasig.awsleaderboard.data.model.Material
import hu.havasig.awsleaderboard.ui.theme.AWSLeaderboardTheme
import hu.havasig.awsleaderboard.ui.theme.AwsOrange
import hu.havasig.awsleaderboard.ui.theme.AwsOrangeLight

@Composable
fun CertDetailScreen(
    onBack: () -> Unit,
    viewModel: CertDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CertDetailContent(
        uiState = uiState,
        onBack = onBack,
        onCompleteMaterial = { materialId -> viewModel.completeMaterial(materialId) }
    )
}

@Composable
fun CertDetailContent(
    uiState: CertDetailUiState,
    onBack: () -> Unit,
    onCompleteMaterial: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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

            uiState.cert != null -> {
                val cert = uiState.cert
                val percentage =
                    (cert.completedMaterials.toFloat() / cert.totalMaterials.toFloat() * 100).toInt()

                // Orange header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AwsOrange)
                        .padding(16.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = cert.shortTitle,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${cert.totalMaterials} Materials Available",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Overall Progress",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$percentage%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Materials list
                Text(
                    text = "LEARNING MATERIALS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn {
                    items(cert.materials) { material ->
                        MaterialRow(
                            material = material,
                            onComplete = { onCompleteMaterial(material.id) },
                            onViewResource = {
                                val intent = Intent(Intent.ACTION_VIEW, material.url.toUri())
                                context.startActivity(intent)
                            }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun MaterialRow(
    material: Material,
    onComplete: () -> Unit,
    onViewResource: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Type icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(AwsOrangeLight, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = materialTypeIcon(material.type),
                contentDescription = null,
                tint = AwsOrange,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = material.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "View Resource ↗",
                fontSize = 12.sp,
                color = AwsOrange,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .clickable { onViewResource() },
            )
        }

        // Complete button
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (material.completed) AwsOrange else AwsOrangeLight,
                    RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { if (!material.completed) onComplete() }) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Complete",
                    tint = if (material.completed) Color.White else AwsOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

fun materialTypeIcon(type: String): ImageVector {
    return when (type) {
        "video" -> Icons.Filled.PlayCircle
        "article" -> Icons.AutoMirrored.Filled.Article
        "practice_exam" -> Icons.Filled.Quiz
        else -> Icons.Filled.Description
    }
}

@AwsPreviews
@Composable
fun CertDetailScreenPreview(
    @PreviewParameter(CertDetailUiStatePreviewParamProvider::class) vertUiStateParameter: CertDetailUiState,
) {
    AWSLeaderboardTheme {
        Surface {
            CertDetailContent(
                uiState = vertUiStateParameter,
                onBack = {},
                onCompleteMaterial = {}
            )
        }
    }
}

class CertDetailUiStatePreviewParamProvider : PreviewParameterProvider<CertDetailUiState> {
    override val values: Sequence<CertDetailUiState> = sequenceOf(
        CertDetailUiState(),
        CertDetailUiState(isLoading = true),
        CertDetailUiState(error = "Something went wrong"),
        CertDetailUiState(
            cert = CertProgress(
                certId = "sa-associate",
                title = "AWS Solutions Architect Associate",
                shortTitle = "SA Associate",
                completedMaterials = 3,
                totalMaterials = 3,
                certified = true,
                materials = listOf(
                    Material(
                        "saa-1",
                        "Introduction to EC2",
                        "video",
                        "https://example.com",
                        true
                    ),
                    Material(
                        "saa-2",
                        "Deep Dive into VPC Networking",
                        "article",
                        "https://example.com",
                        true
                    ),
                    Material(
                        "saa-3",
                        "Practice Exam: IAM and Security",
                        "practice_exam",
                        "https://example.com",
                        false
                    )
                )
            )
        ),
    )
}
