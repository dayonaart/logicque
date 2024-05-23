package id.test.logicque.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class TabBarIconItem(
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val badgeAmount: Int? = null
)

@Composable
fun TabView(
  tabBarItems: List<TabBarIconItem>, navController: NavController, tabHeight: Int? = null
) {
  var selectedTabIndex by rememberSaveable {
    mutableIntStateOf(0)
  }

  NavigationBar(
    windowInsets = WindowInsets.navigationBars,
    modifier = if (tabHeight == null) Modifier.clip(shape = RoundedCornerShape(10.dp)) else Modifier
      .height(
        tabHeight.dp
      )
      .clip(shape = RoundedCornerShape(10.dp))
  ) {
    tabBarItems.forEachIndexed { index, tabBarItem ->
      NavigationBarItem(
        selected = selectedTabIndex == index,
        onClick = {
          selectedTabIndex = index
          navController.navigate(tabBarItem.title) {
            popUpTo(navController.graph.id) {
              saveState = true
            }
            launchSingleTop = true
            restoreState = true
          }
        },
        icon = {
          TabBarIconView(
            selectedIcon = tabBarItem.selectedIcon,
            unselectedIcon = tabBarItem.unselectedIcon,
            isSelected = selectedTabIndex == index
          )
        },
//        colors = NavigationBarItemDefaults.colors()
//          .copy(selectedIconColor = if (mainViewModel.darkMode) Cyan else Blue)
      )
    }
  }
}

@Composable
private fun TabBarIconView(
  isSelected: Boolean,
  selectedIcon: ImageVector,
  unselectedIcon: ImageVector,
  badgeAmount: Int? = null
) {
  BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
    Icon(
      imageVector = if (isSelected) {
        selectedIcon
      } else {
        unselectedIcon
      }, contentDescription = "tab"
    )
  }
}


@Composable
private fun TabBarBadgeView(count: Int? = null) {
  if (count != null) {
    Badge {
      Text(count.toString())
    }
  }
}