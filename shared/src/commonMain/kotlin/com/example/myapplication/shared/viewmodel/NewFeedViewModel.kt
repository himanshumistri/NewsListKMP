package com.example.myapplication.shared.viewmodel


import androidx.compose.runtime.mutableStateListOf
import com.example.myapplication.shared.data.Article
import com.example.myapplication.shared.domain.usecase.NewsFeedUseCase
import com.example.myapplication.shared.getPlatformName
import com.rickclephas.kmm.viewmodel.KMMViewModel

class NewFeedViewModel(streamNewFeed: NewsFeedUseCase) : KMMViewModel() {
    val fetchStreamNews = streamNewFeed()

    private var mNewFeedDataList = mutableStateListOf<Article>()


    fun addAllNews(listItem: MutableList<Article>){
        mNewFeedDataList.clear()
        mNewFeedDataList.addAll(listItem)
    }

    fun getList() : MutableList<Article>{
        return mNewFeedDataList
    }

    fun getHostType():String{
        return getPlatformName()
    }

}