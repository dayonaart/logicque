package id.test.logicque.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.logicque.microservices.data.userpost.UserPost
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.ui.custom.FavoriteFab
import id.test.logicque.ui.custom.SpaceHeight
import id.test.logicque.ui.custom.UserPostView

object Fav {
  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  fun View(innerPad: PaddingValues) {
    Scaffold(
      modifier = Modifier.padding(innerPad),
      floatingActionButton = {
        FavoriteFab.View {
        }
      },
      floatingActionButtonPosition = FabPosition.Start
    ) { ip ->
      val like = mainViewModel.filterLikeList.map { it.postData }
      val userPost = UserPost(
        data = like
//        data = like.subList(
//          mainViewModel.existingLikePage, mainViewModel.likeList.size
//        )
      )
      UserPostView(
        post = userPost,
        useFilterTag = true,
        loading = false,
        modifier = Modifier
          .padding(ip)
          .padding(horizontal = 20.dp),
        header = {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(text = "LIKE POST")
            Text(text = "TOTAL LIKE ${mainViewModel.likeList.size}")
            10.SpaceHeight()
          }
        }, footer = {
//          Box(
//            modifier = Modifier.border(
//              width = 1.dp,
//              color = Color.Gray,
//              shape = RoundedCornerShape(20.dp)
//            )
//          ) {
//            LazyRow(
//              horizontalArrangement = Arrangement.Center,
//              verticalAlignment = Alignment.CenterVertically,
//              modifier = Modifier.fillMaxWidth()
//            ) {
//              stickyHeader {
//                Icon(imageVector = Icons.AutoMirrored.Rounded.List, contentDescription = "list")
//              }
//              items(mainViewModel.likeList.size.div(10) + 1) {
//                TextButton(
//                  onClick = {
//                    mainViewModel.existingLikePage = (it + 1)
//                  }) {
//                  Text(
//                    text = "${it + 1}",
//                    color = if (mainViewModel.existingLikePage == (it + 1)) Color.Green else Color.Gray
//                  )
//                }
//              }
//            }
//          }
        }
      )
    }

  }
}