package com.example.myapplication.shared.viewmodel


import androidx.compose.runtime.mutableStateListOf
import com.example.myapplication.shared.data.Article
import com.example.myapplication.shared.domain.usecase.NewsFeedUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel

class NewFeedViewModel(streamNewFeed: NewsFeedUseCase) : KMMViewModel() {
    val fetchStreamNews = streamNewFeed()

    private var mNewFeedDataList = mutableStateListOf<Article>()


    fun addAllNews(listItem: MutableList<Article>){
        mNewFeedDataList.clear()
        mNewFeedDataList.addAll(listItem)
    }

    fun getList() : List<Article>{
        return mNewFeedDataList.toList()
    }

}