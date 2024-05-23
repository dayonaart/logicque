package id.test.logicque.ui.custom

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.logicque.microservices.data.userpost.PostData
import id.logicque.microservices.data.userpost.UserPost
import id.test.logicque.MainModel
import id.test.logicque.dateFormatter
import id.test.logicque.ui.theme.Typography

@Composable
fun UserPostView(scrollState: ScrollState, post: UserPost?, loading: Boolean) {
  if (loading) {
    ShimmerVerticalView(shimmerSize = 250.dp, itemsSize = 10)
  } else {
    Column(
      modifier = Modifier.verticalScrollbar(scrollState)
    ) {
      post?.data?.take(20)?.forEachIndexed { i, it ->
        Box(
          modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .background(
              shape = RoundedCornerShape(20.dp),
              color = Color.LightGray.copy(alpha = 0.2f)
            )
            .height(250.dp)
            .border(
              width = 1.dp,
              shape = RoundedCornerShape(20.dp),
              color = Color.Gray
            )
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(10.dp),
          ) {
            OwnerView(it = it)
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            5.SpaceHeight()
            ImageDescView(it = it)
            5.SpaceHeight()
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            5.SpaceHeight()
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              items(it?.tags?.size ?: 0) { tIndex ->
                val tag = it?.tags?.get(tIndex)
                ElevatedButton(onClick = {}, modifier = Modifier.height(30.dp)) {
                  Text(text = "$tag", style = Typography.labelSmall)
                }
              }
            }
            10.SpaceHeight()
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            5.SpaceHeight()
            LikeView(it = it)
          }
        }
      }
    }
  }
}

@Composable
private fun OwnerView(it: PostData?) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(end = 10.dp, start = 10.dp),
    horizontalArrangement = Arrangement.Absolute.SpaceBetween
  ) {
    Text(
      text = "${it?.owner?.firstName}",
      style = Typography.labelSmall,
    )
    Text(
      text = "${it?.publishDate?.dateFormatter()}",
      style = Typography.labelSmall,
    )
  }
}

@Composable
private fun ImageDescView(it: PostData?) {
  Row {
    AsyncImage(
      contentScale = ContentScale.FillBounds,
      modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .fillMaxWidth(0.6f)
        .fillMaxHeight(0.6f),
      model = it?.image, contentDescription = it?.id
    )
    5.SpaceWidth()
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.6f)
        .border(width = 1.dp, color = Color.Green, shape = RoundedCornerShape(10.dp))
        .padding(5.dp)
    ) {
      Text(
        text = "${it?.text}",
        overflow = TextOverflow.Ellipsis,
        style = Typography.labelSmall
      )
    }
  }
}

@Composable
private fun LikeView(it: PostData?) {
  Row(
    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    modifier = Modifier
      .fillMaxWidth()
      .padding(end = 10.dp, start = 10.dp)
  ) {
    val hasLike = MainModel.mainViewModel.likeList.find { p -> p.uid == it?.id } != null
    val totalLike = if (hasLike) (it?.likes ?: 0) + 1 else it?.likes
    Text(text = "$totalLike Likes")
    Row(modifier = Modifier.clickable {
      if (hasLike) {
        MainModel.mainViewModel.unlike(it?.id)
        return@clickable
      }
      MainModel.mainViewModel.like(postId = it?.id, data = it)
    }) {
      Icon(
        imageVector = Icons.Rounded.Favorite,
        contentDescription = "fav",
        tint = if (hasLike) Color.Red
        else Color.Black
      )
      8.SpaceWidth()
      Text(text = "Like")
    }
  }
}