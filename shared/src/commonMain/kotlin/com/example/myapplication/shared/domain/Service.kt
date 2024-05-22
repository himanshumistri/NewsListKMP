package com.example.myapplication.shared.domain


import com.example.myapplication.shared.data.NewsResponse
import com.example.myapplication.shared.util.fetch
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod


object Service {
    suspend fun HttpClient.fetNewFeed(page: Int = 1) =
        fetch<NewsResponse> {
            url("/v2/top-headlines?sources=techcrunch&apiKey=")
            method = HttpMethod.Get
        }
}