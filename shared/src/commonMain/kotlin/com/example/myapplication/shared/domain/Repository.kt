package com.example.myapplication.shared.domain

import com.example.myapplication.shared.data.NewsResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun streamNewFeed(): Flow<NewsResponse>
}