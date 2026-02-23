package hu.havasig.awsleaderboard.data.model

data class Material(
    val id: String,
    val title: String,
    val type: String,
    val url: String,
    val completed: Boolean = false
)

data class CertProgress(
    val certId: String,
    val title: String,
    val shortTitle: String,
    val completedMaterials: Int,
    val totalMaterials: Int,
    val certified: Boolean,
    val materials: List<Material>
)