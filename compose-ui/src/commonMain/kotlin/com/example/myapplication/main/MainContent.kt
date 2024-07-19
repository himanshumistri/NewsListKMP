package com.example.myapplication.main


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import com.example.myapplication.shared.data.Article
import com.example.myapplication.shared.getUUID
import com.example.myapplication.shared.main.MainComponent
import com.example.myapplication.shared.viewmodel.NewFeedViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//./gradlew iosArm64Binaries,./gradlew iosArm64MainBinaries
class MainScreen(private val component: MainComponent,val onBackClick:()->Unit) : KoinComponent {
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
                    title = { Text(text = "News List") },
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
                NewsList(viewModel.getList(),viewModel.getHostType(),onBackClick)
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
                    }else{
                        Logger.i(tag = "MainContent",messageString="NewsList is already filled")
                    }
                    Logger.i(tag = "MainContent",messageString="API Response is $it")
                })
            }

            onDispose {

            }
        }
    }

    @Composable
    fun NewsList(newsItems: List<Article>,hotName:String,onBackClick:()->Unit) {
        Logger.i(tag = "MainContent",messageString="NewsList called")
        LazyColumn(Modifier.fillMaxSize()){
            itemsIndexed(newsItems){ index, item ->
                NewsCard(item.imageUrl!!, item.title,hotName,index,onBackClick)
                Spacer(Modifier.height(10.dp))
            }
        }

    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun NewsCard(imageUrl: String, title: String,hotName: String,position:Int,onBackClick:()->Unit) {
        Logger.i(tag = "MainContent",messageString="NewsList Title is $title , hostName $hotName")
        Logger.i(tag = "MainContent",messageString="NewsList Image URL is $imageUrl")

        /*val colorBoarder= if(position.mod(2) == 0){
              Color.Blue
        }else{
            Color.Transparent
        }*/

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .padding(8.dp).border(width = 2.dp, color = Color.Transparent).clickable {
                    //Navigate to next screen
                    onBackClick.invoke()
                    //Logger.i("UUID from target system is ${getUUID()}")
                }
        ) {
            /**
             * modifier = Modifier.onClick(enabled = true){
             *                 //Logger.i("UUID from target system is ${getUUID()}")
             *             }
             */
            Column() {
                if(hotName.contains("Desktop")){
                    Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center){
                        KamelImage(
                            modifier = Modifier
                                .width(500.dp)
                                .height(200.dp),
                            resource = asyncPainterResource(data = imageUrl),
                            contentScale = ContentScale.Crop,
                            contentDescription = "A picture of $title"
                        )
                    }
                }else{
                    CoilImage(
                        //"https://images.unsplash.com/photo-1484446991649-77f7fbd73f1f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1080&q=100"
                        imageModel = { imageUrl },
                        modifier = Modifier.height(190.dp).fillMaxWidth(), // loading a network image or local resource using an URL.
                        requestListener = {
                            object : ImageRequest.Listener {
                                override fun onStart(request: ImageRequest) {
                                    super.onStart(request)
                                    Logger.i(tag = "MainContent",messageString="onStart--> ${request}")
                                }
                                override fun onError(request: ImageRequest, result: ErrorResult) {
                                    Logger.i(tag = "MainContent",messageString="onError--> ${result.throwable}")
                                    super.onError(request, result)

                                }
                            }
                        },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.FillBounds,
                            alignment = Alignment.Center
                        ),
                        loading = {
                            Box(modifier = Modifier. matchParentSize()) {
                                CircularProgressIndicator(   modifier = Modifier.align(Alignment. Center) )
                            }
                        }
                    )
                }

                //loading = {   Box(modifier = Modifier. matchParentSize()) {     CircularProgressIndicator(        modifier = Modifier. align(Alignment. Center)     )   } }
                /*Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = "News Image",
                    modifier = Modifier.fillMaxSize()
                )*/
                Text(
                    text = title,
                    style = MaterialTheme.typography.caption,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )

            }


        }
    }

}


