package id.test.logicque.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import id.logicque.microservices.capitalizes
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.ui.custom.ShimmerBox


object Home {
  @Composable
  fun View(innerPad: PaddingValues, navController: NavController) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier.padding(innerPad),
      contentPadding = PaddingValues(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(mainViewModel.user.size) {
        val user = mainViewModel.user[it]
        ShimmerBox(modifier = Modifier.height(150.dp), onClick = {
          mainViewModel.getUserDetail("${user?.id}")
          navController.navigate("user_detail")
        }, content = {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Image(
              painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = user?.picture)
                  .apply(block = fun ImageRequest.Builder.() {
                    size(Size.ORIGINAL)
                  }).build()
              ),
              contentDescription = null,
              contentScale = ContentScale.FillBounds,
              modifier = Modifier
                .fillMaxSize(0.7f)
                .padding(5.dp)
            )
            Text(
              maxLines = 1, overflow = TextOverflow.Ellipsis, text = "${
                user?.title?.capitalizes()
              }. ${user?.firstName} ${user?.lastName}", textAlign = TextAlign.Center
            )
          }
        }, hasLoading = mainViewModel.getUserLoading)
      }
      item {
        LaunchedEffect(true) {
          mainViewModel.extendUser()
        }
      }
    }
  }
}