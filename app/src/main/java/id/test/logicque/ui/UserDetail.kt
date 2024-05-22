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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.logicque.microservices.capitalizes
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.dateFormatter
import id.test.logicque.ui.custom.ShimmerBox
import id.test.logicque.ui.theme.Typography
import java.util.Locale

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
    OutlinedTextField(placeholder = {
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
    ShimmerBox(content = {
      val user = mainViewModel.userDetail
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        AsyncImage(
          model = user?.picture,
          contentDescription = "avatar",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize(0.4f)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
          text = "${user?.title?.capitalizes()}. ${user?.firstName} ${user?.lastName}",
          style = Typography.titleLarge
        )
        ElevatedButton(colors = ButtonColors(
          containerColor = if (user?.hasFriend == true) Color.Red else Color.Green,
          disabledContainerColor = Color.Gray,
          disabledContentColor = Color.Gray,
          contentColor = Color.White
        ), onClick = {
          if (user?.hasFriend == true) {
            mainViewModel.removeFriend(user)
            return@ElevatedButton
          }
          mainViewModel.addFriend(user)
        }) {
          Text(text = if (user?.hasFriend == true) "remove" else "add")
        }
      }
    },
      hasLoading = mainViewModel.getUserDetailLoading,
      modifier = if (mainViewModel.getUserDetailLoading) Modifier
        .fillMaxWidth()
        .height(250.dp) else Modifier.padding(
        top = 20.dp
      ),
      onClick = {})
  }

  @Composable
  fun UserInfo() {
    val user = mainViewModel.userDetail
    ShimmerBox(content = {
      Column(modifier = Modifier.fillMaxWidth(0.7f)) {
        Text(text = "Gender : ${user?.gender}", style = Typography.labelMedium)
        Spacer(modifier = Modifier.height(5.dp))
        Text(
          text = "Date of birth : ${user?.dateOfBirth?.dateFormatter()}",
          style = Typography.labelMedium
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
          text = "Join from : ${user?.registerDate?.dateFormatter()}",
          style = Typography.labelMedium
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "Email : ${user?.email}", style = Typography.labelMedium)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "Address : ${user?.locationToString()}", style = Typography.labelMedium)
      }
    },
      hasLoading = mainViewModel.getUserDetailLoading,
      modifier = if (mainViewModel.getUserDetailLoading) Modifier
        .fillMaxWidth()
        .height(100.dp) else Modifier,
      onClick = {})
  }

  @Composable
  private fun UserPostView() {
    val post =
      if (mainViewModel.userPostFilter == null) mainViewModel.userPost else mainViewModel.userPostFilter
    post?.data?.forEach {
      ShimmerBox(modifier = if (mainViewModel.getUserDetailLoading) Modifier
        .fillMaxWidth()
        .height(250.dp) else Modifier.padding(
        bottom = 10.dp
      ), content = {
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
          Row(modifier = Modifier.clickable {
            if (it?.hasLike == true) {
              mainViewModel.unlikePost(it)
              return@clickable
            }
            mainViewModel.likePost(it)
          }) {
            Icon(
              imageVector = Icons.Rounded.Favorite,
              contentDescription = "fav",
              tint = if (it?.hasLike == true) Color.Red else Color.Black
            )
            Text(text = "Like")
          }
        }
      }, hasLoading = mainViewModel.getUserDetailLoading, onClick = { })
    }
  }
}