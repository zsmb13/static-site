package co.zsmb.staticsite.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "articles")
data class Article(
        val title: String,
        val url: String,
        val content: String,
        val summary: String,
        val publishDate: Date,
        val lastModificationDate: String? = null,
        @Id val id: String? = null)
