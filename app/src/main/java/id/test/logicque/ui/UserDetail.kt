package id.test.logicque.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.capitalizes
import id.test.logicque.dateFormatter
import id.test.logicque.ui.custom.ShimmerBox
import id.test.logicque.ui.custom.ShimmerImageRounded
import id.test.logicque.ui.custom.ShimmerVerticalView
import id.test.logicque.ui.custom.SpaceHeight
import id.test.logicque.ui.custom.SpaceWidth
import id.test.logicque.ui.theme.Typography

object UserDetail {
  @Composable
  fun View(innerPad: PaddingValues) {
    Scaffold(modifier = Modifier
      .padding(innerPad)
      .padding(start = 10.dp, end = 10.dp), topBar = {
      SearchField()
    }) { ip ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(ip)
          .verticalScroll(rememberScrollState()),
      ) {
        UserProfile()
        Spacer(modifier = Modifier.height(20.dp))
        UserInfo()
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))
        UserPostView()
      }
    }
  }

  @Composable
  fun SearchField() {
    val ctx = LocalContext.current
    val fieldFocus = LocalFocusManager.current
    OutlinedTextField(
      placeholder = {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = "Search in ${mainViewModel.userDetail?.firstName}'s Post",
          textAlign = TextAlign.Center,
          color = if (mainViewModel.darkMode) Color.Cyan else Color.Blue
        )
      },
      prefix = {
        Icon(
          imageVector = Icons.Rounded.Search,
          contentDescription = "search",
          tint = if (mainViewModel.darkMode) Color.Cyan else Color.Blue
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        .background(color = Color.LightGray, shape = RoundedCornerShape(25.dp)),
      value = mainViewModel.searchController,
      onValueChange = mainViewModel::onSearchPostChange,
      shape = RoundedCornerShape(25.dp),
      keyboardActions = KeyboardActions(onSearch = {
        mainViewModel.onSearchPost(ctx)
        fieldFocus.clearFocus()
      }),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
  }

  @Composable
  fun UserProfile() {
    val user = mainViewModel.userDetail
    if (mainViewModel.getUserDetailLoading) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        10.SpaceHeight()
        ShimmerImageRounded(shimmerSize = 200.dp)
        10.SpaceHeight()
        ShimmerBox(
          modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(30.dp)
        )
      }
    } else {
      val hasFriend = mainViewModel.listFriend.find { p -> p.userId == user?.id } != null
      Box(contentAlignment = Alignment.CenterStart) {
        ElevatedButton(onClick = {
          if (hasFriend) {
            mainViewModel.removeFriend(user?.id)
            return@ElevatedButton
          }
          mainViewModel.addFriend(user?.id, data = user)
        }) {
          Text(text = if (hasFriend) "Remove" else "Add")
        }
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          10.SpaceHeight()
          AsyncImage(
            model = user?.picture, contentDescription = user?.firstName, modifier = Modifier
              .clip(CircleShape)
              .size(200.dp)
          )
          10.SpaceHeight()
          Text(
            text = "${user?.title?.capitalizes()}. ${user?.firstName} ${user?.lastName}",
            style = Typography.titleMedium
          )
        }
      }
    }
  }

  @Composable
  fun UserInfo() {
    val user = mainViewModel.userDetail
    if (mainViewModel.getUserDetailLoading) {
      ShimmerBox(
        modifier = Modifier
          .fillMaxWidth()
          .height(150.dp)
      )
    } else {
      Box(
        modifier = Modifier
          .border(
            width = 1.dp,
            shape = RoundedCornerShape(20.dp),
            color = Color.Gray
          )
          .background(
            shape = RoundedCornerShape(20.dp),
            color = Color.LightGray.copy(alpha = 0.3f)
          )
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text(text = "Gender : ${user?.gender}", style = Typography.labelMedium)
          5.SpaceHeight()
          Text(
            text = "Date of birth : ${user?.dateOfBirth?.dateFormatter()}",
            style = Typography.labelMedium
          )
          5.SpaceHeight()
          Text(
            text = "Join from : ${user?.registerDate?.dateFormatter()}",
            style = Typography.labelMedium
          )
          5.SpaceHeight()
          Text(text = "Email : ${user?.email}", style = Typography.labelMedium)
          5.SpaceHeight()
          Text(text = "Address : ${user?.locationToString()}", style = Typography.labelMedium)
        }
      }
    }
  }

  @Composable
  private fun UserPostView() {
    val post = mainViewModel.filterUserPost
    if (mainViewModel.getUserPostLoading) {
      ShimmerVerticalView(shimmerSize = 250.dp, itemsSize = 10)
    } else {
      post?.data?.forEach {
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
                text = "${it?.owner?.firstName}",
                style = Typography.labelSmall,
                color = Color.Blue
              )
              Text(
                text = "${it?.publishDate?.dateFormatter()}",
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
                model = it?.image, contentDescription = it?.id
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
                  text = "${it?.text}",
                  overflow = TextOverflow.Ellipsis,
                  style = Typography.labelSmall
                )
              }
            }
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
            Row(
              horizontalArrangement = Arrangement.Absolute.SpaceBetween,
              modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp)
            ) {
              val hasLike = mainViewModel.likeList.find { p -> p.uid == it?.id } != null
              val totalLike = if (hasLike) (it?.likes ?: 0) + 1 else it?.likes
              Text(text = "$totalLike Likes")
              Row(modifier = Modifier.clickable {
                if (hasLike) {
                  mainViewModel.unlike(it?.id)
                  return@clickable
                }
                mainViewModel.like(postId = it?.id, data = it)
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