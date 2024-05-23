package id.test.logicque.ui.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.test.logicque.MainModel

object CustomFab {
  @Composable
  fun View(page: Int, onClick: (next: Boolean) -> Unit) {
    var sortExpanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
      ExtendedFloatingActionButton(
        modifier = Modifier.padding(top = 10.dp),
        onClick = { sortExpanded = !sortExpanded },
        icon = { Icon(Icons.Filled.Menu, "Sort") },
        text = { Text(text = "Options") },
      )
      DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
        DropdownMenuItem(
          text = { Text(if (MainModel.mainViewModel.darkMode) "Light mode" else "Dark Mode") },
          onClick = {
            MainModel.mainViewModel.changeTheme()
            sortExpanded = false
          })
        HorizontalDivider()
        if (page != 4) {
          DropdownMenuItem(text = { Text("Load Next") }, onClick = {
            onClick(true)
            sortExpanded = false
          })
          HorizontalDivider()
        }
        if (page != 0) {
          DropdownMenuItem(text = { Text("Load Before") }, onClick = {
            onClick(false)
            sortExpanded = false
          })
        }
      }
    }
  }
}