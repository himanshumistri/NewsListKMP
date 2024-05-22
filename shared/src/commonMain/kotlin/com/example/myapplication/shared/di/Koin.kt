package com.example.myapplication.shared.di


import com.example.myapplication.shared.domain.Repository
import com.example.myapplication.shared.domain.RepositoryImpl
import com.example.myapplication.shared.domain.usecase.NewsFeedUseCase
import com.example.myapplication.shared.viewmodel.NewFeedViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf


import org.koin.dsl.module

fun initKoin() = startKoin {
    modules(
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule
    )
}

private val networkModule = module {
    single {
        HttpClient {
            defaultRequest {
                url.takeFrom(URLBuilder().takeFrom("https://newsapi.org"))
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
        }
    }
}

private val useCaseModule = module {
    singleOf(::NewsFeedUseCase)
}

private val repositoryModule = module {

    singleOf(::RepositoryImpl){
        bind<Repository>()
    }
    /*singleOf(::RepositoryImpl){
        bind<Repository>()
    }*/
}

private val viewModelModule = module {
    factory { NewFeedViewModel(get()) }
}
