package id.test.logicque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.test.logicque.ui.Fav
import id.test.logicque.ui.Feed
import id.test.logicque.ui.Home
import id.test.logicque.ui.TabBarIconItem
import id.test.logicque.ui.TabView
import id.test.logicque.ui.UserDetail
import id.test.logicque.ui.theme.LogicqueTheme

class MainActivity : ComponentActivity() {
  private val mainViewModel by lazy { MainViewModel() }

  private val homeTab = TabBarIconItem(
    title = "Home",
    selectedIcon = Icons.Default.AccountCircle,
    unselectedIcon = Icons.Default.AccountCircle
  )
  private val feedTab = TabBarIconItem(
    title = "Feed", selectedIcon = Icons.Default.Menu, unselectedIcon = Icons.Default.Menu
  )
  private val settingsTab = TabBarIconItem(
    title = "Setting",
    selectedIcon = Icons.Default.Favorite,
    unselectedIcon = Icons.Default.FavoriteBorder
  )
  private val tabBarItems = listOf(homeTab, feedTab, settingsTab)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainViewModel.init(this)
    setContent {
      val navController = rememberNavController()
      LogicqueTheme {
        Scaffold(
          bottomBar = { TabView(tabBarItems, navController) }, modifier = Modifier.fillMaxSize()
        ) { innerPad ->
          NavHost(navController = navController, startDestination = homeTab.title) {
            composable(homeTab.title) {
              Home.View(innerPad = innerPad, navController = navController)
            }
            composable(feedTab.title) {
              Feed.View(innerPad = innerPad)
            }
            composable(settingsTab.title) {
              Fav.View(innerPad = innerPad)
            }
            composable(
              "user_detail",
            ) {
              UserDetail.View(
                innerPad = innerPad,
              )
            }
          }
        }
      }
    }
  }
}
