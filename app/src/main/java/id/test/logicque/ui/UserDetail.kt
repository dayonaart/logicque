package id.test.logicque.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.capitalizes
import id.test.logicque.dateFormatter
import id.test.logicque.ui.custom.ShimmerBox
import id.test.logicque.ui.custom.ShimmerImageRounded
import id.test.logicque.ui.custom.SpaceHeight
import id.test.logicque.ui.custom.UserPostView
import id.test.logicque.ui.theme.Typography

object UserDetail {
  @Composable
  fun View(innerPad: PaddingValues) {
    val scrollState = rememberScrollState()
    Scaffold(modifier = Modifier
      .padding(innerPad)
      .padding(start = 10.dp, end = 10.dp), topBar = {
      SearchField()
    }) { ip ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(ip)
          .verticalScroll(scrollState),
      ) {
        UserProfile()
        Spacer(modifier = Modifier.height(20.dp))
        UserInfo()
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))
        UserPostView(scrollState, mainViewModel.filterUserPost, mainViewModel.getUserPostLoading)
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
        )
      },
      prefix = {
        Icon(
          imageVector = Icons.Rounded.Search,
          contentDescription = "search",
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp, top = 20.dp),
      value = mainViewModel.searchController,
      onValueChange = mainViewModel::onSearchPostChange,
      shape = RoundedCornerShape(25.dp),
      keyboardActions = KeyboardActions(onSearch = {
        mainViewModel.onSearchPost(ctx)
        fieldFocus.clearFocus()
      }),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
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
        ElevatedButton(
          onClick = {
            if (hasFriend) {
              mainViewModel.removeFriend(user?.id)
              return@ElevatedButton
            }
            mainViewModel.addFriend(user?.id, data = user)
          }, colors = ButtonColors(
            containerColor = if (hasFriend) Color.Red else Color.Green,
            contentColor = Color.Unspecified,
            disabledContentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified
          )
        ) {
          Text(
            text = if (hasFriend) "Remove Friend" else "Add Friend",
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
}