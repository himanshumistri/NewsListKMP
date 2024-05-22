package com.example.myapplication.main


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.example.myapplication.shared.data.Article
import com.example.myapplication.shared.main.MainComponent
import com.example.myapplication.shared.viewmodel.NewFeedViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreen(private val component: MainComponent) : KoinComponent {
    private val viewModel: NewFeedViewModel by inject()


    @Composable
    fun MainContent(
    )
    {
       // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
        val scope = rememberCoroutineScope()

        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = { Text(text = "Decompose Template") },
                )
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                /*Button(onClick = component::onShowWelcomeClicked) {
                    Text(text = "Show Welcome screen")
                }*/
                Logger.i(tag = "MainContent",messageString="Main Box")
                NewsList(viewModel.getList())
            }
        }

        DisposableEffect("MainContent"){

            Logger.i(tag = "MainContent",messageString="DisposableEffect called")

            scope.launch {
                Logger.i(tag = "MainContent",messageString="NewsList Flow collect called")
                viewModel.fetchStreamNews.collect(FlowCollector {
                    if(it.articles.isNotEmpty()){
                        viewModel.addAllNews(it.articles.toMutableList())
                        Logger.i(tag = "MainContent",messageString="NewsList added to VM")
                    }
                    Logger.i(tag = "MainContent",messageString="API Response is $it")
                })
            }

            onDispose {

            }
        }
    }

    @Composable
    fun NewsList(newsItems: List<Article>) {
        Logger.i(tag = "MainContent",messageString="NewsList called")
        LazyColumn(Modifier.fillMaxSize()){
            items(newsItems){
                NewsCard(it.imageUrl!!, it.title)
                Spacer(Modifier.height(10.dp))
            }
        }

        /*LazyColumn {
            /*items(newsItems) { item ->

            }*/
        }*/
    }

    @Composable
    fun NewsCard(imageUrl: String, title: String) {
        Logger.i(tag = "MainContent",messageString="NewsList Title is $title")
        Logger.i(tag = "MainContent",messageString="NewsList Image URL is $imageUrl")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {

                CoilImage(
                    imageModel = { imageUrl },
                    modifier = Modifier.height(150.dp).fillMaxWidth(), // loading a network image or local resource using an URL.
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillBounds,
                        alignment = Alignment.Center
                    )
                )
                /*Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = "News Image",
                    modifier = Modifier.fillMaxSize()
                )*/
                Text(
                    text = title,
                    style = MaterialTheme.typography.caption,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(5.dp)
                )

            }


        }
    }

}


