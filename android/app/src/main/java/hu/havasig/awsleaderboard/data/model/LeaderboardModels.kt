package hu.havasig.awsleaderboard.data.model

data class LeaderboardEntry(
    val username: String,
    val certsEarned: Int,
    val materialsCompleted: Int
)