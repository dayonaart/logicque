package id.test.logicque.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.capitalizes
import id.test.logicque.ui.custom.ShimmerVerticalGridView
import id.test.logicque.ui.custom.SpaceHeight


object Home {
  @SuppressLint("CoroutineCreationDuringComposition")
  @Composable
  fun View(innerPad: PaddingValues, navController: NavController) {
    if (mainViewModel.getUserLoading) {
      ShimmerVerticalGridView(
        modifier = Modifier.padding(innerPad),
        itemSize = 20,
        shimmerSize = 150.dp
      )
    } else {
      Scaffold(modifier = Modifier.padding(innerPad), floatingActionButton = {
        Fab()
      }) {
        UserList(innerPad = it, navController = navController)
      }
    }
  }

  @Composable
  private fun UserList(innerPad: PaddingValues, navController: NavController) {
    val scrollState = rememberLazyGridState()
    LazyVerticalGrid(
      modifier = Modifier.padding(innerPad),
      columns = GridCells.Fixed(2),
      state = scrollState,
      contentPadding = PaddingValues(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(mainViewModel.user?.data?.size ?: 0) {
        val user = mainViewModel.user?.data!![it]
        Box(
          modifier = Modifier
            .clickable {
              mainViewModel.getUserDetail("${user?.id}")
              mainViewModel.getUserPost("${user?.id}")
              navController.navigate("user_detail")
            }
            .border(
              width = 1.dp,
              shape = RoundedCornerShape(10),
              color = Color.Gray
            )
            .size(150.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            AsyncImage(
              modifier = Modifier
                .background(shape = RoundedCornerShape(10.dp), color = Color.LightGray)
                .fillMaxSize(0.8f),
              model = user?.picture,
              contentDescription = user?.title
            )
            5.SpaceHeight()
            HorizontalDivider()
            Text(text = "${user?.title?.capitalizes()}. ${user?.firstName} ${user?.lastName}")
          }
        }
      }
    }
  }

  @Composable
  private fun Fab() {
    var sortExpanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
      ExtendedFloatingActionButton(
        modifier = Modifier.padding(top = 10.dp),
        onClick = { sortExpanded = !sortExpanded },
        icon = { Icon(Icons.Filled.Menu, "Sort") },
        text = { Text(text = "Options") },
      )
      DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
        if (mainViewModel.userPage != 4) {
          DropdownMenuItem(text = { Text("Load Next") }, onClick = {
            mainViewModel.getUser(true)
            sortExpanded = false
          })
          HorizontalDivider()
        }
        if (mainViewModel.userPage != 0) {
          DropdownMenuItem(text = { Text("Load Before") }, onClick = {
            mainViewModel.getUser(false)
            sortExpanded = false
          })
        }
      }
    }
  }
}