package com.example.myapplication.shared.domain

import co.touchlab.kermit.Logger

import com.example.myapplication.shared.domain.Service.fetNewFeed
import com.example.myapplication.shared.data.NewsResponse
import com.example.myapplication.shared.util.Result
import com.example.myapplication.shared.util.map

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RepositoryImpl(private val apiService: HttpClient) : Repository {
    override fun streamNewFeed(): Flow<NewsResponse> = flow {
        Logger.i(tag = "MainContent",messageString="API Called from Flow block")
        val result =apiService.fetNewFeed(1)
        if(result is Result.Success){
            val resultFinal = result.map {
                emit(it)
            }
        }
    }
}