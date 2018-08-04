package co.zsmb.site.backend.data

import kotlinx.serialization.Serializable

@Serializable
data class ArticleSummary(
        val title: String,
        val url: String,
        val summary: String,
        val publishDate: Long,
        val id: String
)

@Serializable
data class ArticleDetail(
        val title: String,
        val content: String,
        val publishDate: Long,
        val lastModificationDate: Long? = null,
        val id: String
)

@Serializable
data class OldCommonCustomPage(
        val name: String,
        val content: String
)
