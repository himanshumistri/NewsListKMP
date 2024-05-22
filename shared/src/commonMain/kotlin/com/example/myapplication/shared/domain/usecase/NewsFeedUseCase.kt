package com.example.myapplication.shared.domain.usecase

import com.example.myapplication.shared.domain.Repository

/**
 * Here DI Inject will Pass Object of RepositoryImpl, So Here Repository will be object of
 * RepositoryImpl
 */
class NewsFeedUseCase(private val repository: Repository) {
    operator fun invoke() = repository.streamNewFeed()
}