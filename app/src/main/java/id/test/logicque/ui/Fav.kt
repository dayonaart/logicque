package id.test.logicque.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.logicque.microservices.data.userpost.UserPost
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.ui.custom.SpaceHeight
import id.test.logicque.ui.custom.UserPostView
import id.test.logicque.ui.theme.Typography

object Fav {
  @OptIn(ExperimentalLayoutApi::class)
  @Composable
  fun View(innerPad: PaddingValues) {
    val scrollState = rememberScrollState()
    val tags =
      mainViewModel.filterLikeList.map { it.postData?.tags?.joinToString()?.trim() }.joinToString()
        .replace(" ", "").split(",").toSet().toList().filter { it != "" }
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPad)
        .padding(20.dp)
        .verticalScroll(scrollState),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = "LIKE POST")
      10.SpaceHeight()
      HorizontalDivider(thickness = 3.dp)
      10.SpaceHeight()
      if (tags.isNotEmpty()) {
        Box(
          modifier = Modifier
            .align(alignment = Alignment.Start)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
        ) {
          Column {
            Text(text = "Filter by tag", style = Typography.labelMedium)
            10.SpaceHeight()
            FlowRow(maxItemsInEachRow = 3, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              tags.forEach {
                ElevatedButton(onClick = {
                  mainViewModel.filterLikes(it)
                }) {
                  Text(text = it)
                }
              }
            }
          }
        }
        10.SpaceHeight()
        HorizontalDivider(thickness = 2.dp)
        10.SpaceHeight()
        val like = mainViewModel.filterLikeList.map { it.postData }
        val userPost = UserPost(data = like)
        UserPostView(scrollState = scrollState, post = userPost, loading = false)
      }
    }
  }
}