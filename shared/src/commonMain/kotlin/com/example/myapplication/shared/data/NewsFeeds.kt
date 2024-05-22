package com.example.myapplication.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String?,
    val name: String
)

@Serializable
data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    @SerialName("urlToImage")
    val imageUrl: String?,
    @SerialName("publishedAt")
    val publishDate: String,
    val content: String
)

@Serializable
data class NewsResponse(
    val status: String,
    @SerialName("totalResults")
    val totalResults: Int,
    val articles: List<Article>
)
