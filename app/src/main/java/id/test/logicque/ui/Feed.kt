package id.test.logicque.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.ui.custom.CustomFab
import id.test.logicque.ui.custom.UserPostView

object Feed {
  @Composable
  fun View(innerPad: PaddingValues) {
    val scrollState = rememberScrollState()
    Scaffold(modifier = Modifier.padding(innerPad), floatingActionButton = {
      CustomFab.View(mainViewModel.postPage) {
        mainViewModel.getPost(it)
      }
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
}