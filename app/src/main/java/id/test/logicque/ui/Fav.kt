package id.test.logicque.ui

import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.dateFormatter
import id.test.logicque.ui.custom.ShimmerBox
import id.test.logicque.ui.theme.Typography

object Fav {
  @Composable
  fun View(innerPad: PaddingValues) {
    LaunchedEffect("getlikes") {
      mainViewModel.getPostLike()
    }
    Column(
      modifier = Modifier
        .padding(innerPad)
        .padding(20.dp)
        .verticalScroll(rememberScrollState())
    ) {
      UserPostView()
    }
  }

  @Composable
  private fun UserPostView() {
    val post = mainViewModel.postLikeList
    post.forEach {
      ShimmerBox(modifier = Modifier.padding(bottom = 10.dp), content = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Rounded.Person, contentDescription = "person")
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "${it?.owner?.firstName}", style = Typography.labelSmall, color = Color.Blue
              )
              Text(
                text = "${it?.publishDate?.dateFormatter()}",
                style = Typography.labelSmall,
                color = Color.Blue
              )
            }
          }
          Spacer(modifier = Modifier.height(5.dp))
          AsyncImage(
            model = it?.image,
            contentDescription = "post",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.height(200.dp)
          )
          Spacer(modifier = Modifier.height(5.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            items(it?.tags?.size ?: 0) { i ->
              ElevatedButton(
                onClick = {}, colors = ButtonColors(
                  containerColor = Color.Blue,
                  contentColor = Color.White,
                  disabledContentColor = Color.Gray,
                  disabledContainerColor = Color.Gray
                )
              ) {
                Text(text = "${it?.tags?.get(i)}")
              }
            }
          }
          Spacer(modifier = Modifier.height(5.dp))
          Text(text = "${it?.text}")
          Spacer(modifier = Modifier.height(5.dp))
          Text(text = "${it?.likes} Likes")
          Spacer(modifier = Modifier.height(5.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(8.dp))
        }
      }, hasLoading = false, onClick = { })
    }
  }
}