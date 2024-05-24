package id.test.logicque.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.ui.custom.UserPostView
import kotlinx.coroutines.launch

object Feed {
  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  fun View(innerPad: PaddingValues) {
    Scaffold(
      modifier = Modifier
        .padding(innerPad)
        .padding(horizontal = 20.dp)
    ) {
      UserPostView(
        modifier = Modifier
          .padding(it)
          .padding(top = 10.dp),
        post = mainViewModel.filterPost,
        loading = mainViewModel.getPostLoading,
        header = {}, footer = {
          Box(
            modifier = Modifier.border(
              width = 1.dp,
              color = Color.Gray,
              shape = RoundedCornerShape(5.dp)
            )
          ) {
            val scrollPage = rememberLazyListState()
            val coroutine = rememberCoroutineScope()
            LazyRow(
              state = scrollPage,
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              stickyHeader {
                Icon(imageVector = Icons.AutoMirrored.Rounded.List, contentDescription = "list")
              }
              items(mainViewModel.postLimit.div(20) + 1) {
                TextButton(
                  onClick = {
                    mainViewModel.existingPostPage = it
                    mainViewModel.getPost()
                    coroutine.launch {
                      scrollPage.scrollToItem(it)
                    }
                  }) {
                  Text(
                    text = "${it + 1}",
                    color = if (mainViewModel.existingPostPage == it) Color.Green else Color.Gray
                  )
                }
              }
            }
          }
        }
      )
    }
  }
}