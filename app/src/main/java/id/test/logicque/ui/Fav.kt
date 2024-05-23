package id.test.logicque.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.dateFormatter
import id.test.logicque.ui.custom.ShimmerVerticalView
import id.test.logicque.ui.custom.SpaceHeight
import id.test.logicque.ui.custom.SpaceWidth
import id.test.logicque.ui.theme.Typography

object Fav {
  @OptIn(ExperimentalLayoutApi::class)
  @Composable
  fun View(innerPad: PaddingValues) {
    val tags =
      mainViewModel.likeList.map { m -> m.postData?.tags }.joinToString().trim()
        .replace("]", "").replace("[", "").split(",").toSet().toList()
    Column(
      modifier = Modifier
        .padding(innerPad)
        .padding(20.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = "LIKE POST")
      if (tags.isNotEmpty())
        Box(
          modifier = Modifier
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
        ) {
          FlowRow(maxItemsInEachRow = 3, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            tags.forEach {
              ElevatedButton(onClick = {
                mainViewModel.filterLikes(it.trim())
              }) {
                Text(text = it)
              }
            }
          }
        }
      10.SpaceHeight()
      HorizontalDivider(thickness = 2.dp)
      10.SpaceHeight()
      LikeViews()
    }
  }

  @Composable
  fun LikeViews() {
    if (mainViewModel.filterLikeList.isEmpty()) {
      ShimmerVerticalView(shimmerSize = 250.dp, itemsSize = 10)
    } else {
      mainViewModel.filterLikeList.forEach {
        val item = it.postData
        Box(
          modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .background(
              shape = RoundedCornerShape(20.dp),
              color = Color.LightGray.copy(alpha = 0.3f)
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
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp),
              horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
              Text(
                text = "${item?.owner?.firstName}",
                style = Typography.labelSmall,
                color = Color.Blue
              )
              Text(
                text = "${item?.publishDate?.dateFormatter()}",
                style = Typography.labelSmall,
                color = Color.Blue
              )
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            5.SpaceHeight()
            Row {
              AsyncImage(
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .fillMaxWidth(0.6f)
                  .fillMaxHeight(0.6f),
                model = item?.image, contentDescription = item?.id
              )
              5.SpaceWidth()
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .fillMaxHeight(0.6f)
                  .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                  .padding(5.dp)
              ) {
                Text(
                  text = "${item?.text}",
                  overflow = TextOverflow.Ellipsis,
                  style = Typography.labelSmall
                )
              }
            }
            5.SpaceHeight()
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            5.SpaceHeight()
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              items(item?.tags?.size ?: 0) { tIndex ->
                val tag = item?.tags?.get(tIndex)
                ElevatedButton(onClick = {}, modifier = Modifier.height(30.dp)) {
                  Text(text = "$tag", style = Typography.labelSmall)
                }
              }
            }
            10.SpaceHeight()
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            5.SpaceHeight()
            Row(
              horizontalArrangement = Arrangement.Absolute.SpaceBetween,
              modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp)
            ) {
              val hasLike = mainViewModel.likeList.find { p -> p.uid == item?.id } != null
              Text(text = "${(item?.likes ?: 0) + 1} Likes")
              Row(modifier = Modifier.clickable {
                mainViewModel.unlike(item?.id)
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
        }
      }
    }
  }
}