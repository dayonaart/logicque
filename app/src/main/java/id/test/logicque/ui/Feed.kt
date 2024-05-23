package id.test.logicque.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.ui.custom.UserPostView

object Feed {
  @Composable
  fun View(innerPad: PaddingValues) {
    val scrollState = rememberScrollState()
    Scaffold(modifier = Modifier.padding(innerPad), floatingActionButton = {
      Fab()
    }) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(it)
          .padding(20.dp)
          .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        UserPostView(
          scrollState = scrollState,
          post = mainViewModel.filterPost,
          loading = mainViewModel.getPostLoading
        )
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
        DropdownMenuItem(text = { Text("Load Next") }, onClick = {
          mainViewModel.getPost(true)
          sortExpanded = false
        })
        HorizontalDivider()
        if (mainViewModel.postPage != 0) {
          DropdownMenuItem(text = { Text("Load Before") }, onClick = {
            mainViewModel.getPost(false)
            sortExpanded = false
          })
        }
      }
    }
  }
}