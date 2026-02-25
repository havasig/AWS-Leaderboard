package hu.havasig.awsleaderboard.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light",
    fontScale = 1f,
    showBackground = true,
    locale = "hu",
)
@Preview(
    name = "Night",
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    locale = "en",
)
annotation class AwsPreviews
