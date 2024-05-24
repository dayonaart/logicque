package id.test.logicque.ui.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.test.logicque.MainModel.mainViewModel
import id.test.logicque.ui.theme.Typography

object FavoriteFab {
  @OptIn(ExperimentalLayoutApi::class)
  @Composable
  fun View(onClick: () -> Unit) {
    val tags =
      mainViewModel.likeList.map { it.postData?.tags?.joinToString()?.trim() }
        .joinToString()
        .replace(" ", "").split(",").toSet().toList().filter { it != "" }
    var menuExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }
    Column {
      ExtendedFloatingActionButton(
        modifier = Modifier.padding(top = 10.dp),
        onClick = { menuExpanded = !menuExpanded },
        icon = { Icon(Icons.Filled.Menu, "Sort") },
        text = { Text(text = "Menu") },
      )
      DropdownMenu(
        modifier = Modifier.clip(RectangleShape),
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false }) {
        DropdownMenuItem(
          text = {
            Text(
              if (mainViewModel.darkMode) "Light mode" else "Dark Mode",
              style = Typography.labelMedium
            )
          },
          onClick = {
            mainViewModel.changeTheme()
            menuExpanded = false
          })
        HorizontalDivider()
        DropdownMenuItem(text = { Text("Sort", style = Typography.labelMedium) }, onClick = {
          onClick()
          menuExpanded = false
          sortExpanded = true
        })
      }
      DropdownMenu(
        modifier = Modifier
          .fillMaxWidth(0.5f)
          .fillMaxHeight(0.2f)
          .clip(RectangleShape),
        expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "Sort By Tag")
          HorizontalDivider()
          FlowRow(maxItemsInEachRow = 2) {
            tags.forEach {
              DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = {
                  TextButton(
                    modifier = Modifier
                      .background(
                        color = if (mainViewModel.filterLikeKey.contains(it)) Color.Green else Color.Unspecified,
                        shape = RoundedCornerShape(90.dp)
                      )
                      .height(30.dp),
                    onClick = {
                      mainViewModel.filterLikes(it)
                    },
                    border = BorderStroke(width = 1.dp, color = Color.Gray),
                  ) {
                    Text(
                      it,
                      style = Typography.labelSmall,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1
                    )
                  }
                },
                onClick = {
                  sortExpanded = false
                })
            }
          }
        }
      }
    }
  }
}