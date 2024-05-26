package id.test.logicque

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.logicque.microservices.MicroService
import id.logicque.microservices.data.User
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.PostData
import id.logicque.microservices.data.userpost.UserPost
import id.logicque.microservices.network.CoreError
import id.logicque.microservices.network.CoreException
import id.logicque.microservices.network.CoreSuccess
import id.logicque.microservices.network.CoreTimeout
import id.logicque.microservices.network.Loading
import id.test.logicque.MainModel.database
import id.test.logicque.dao.DatabaseDao
import id.test.logicque.dao.Friend
import id.test.logicque.dao.Likes
import id.test.logicque.dao.TypeConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Database(entities = [Friend::class, Likes::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun databaseDao(): DatabaseDao
}

object MainModel {
  lateinit var mainViewModel: MainViewModel
  lateinit var database: AppDatabase
}

class MainViewModel : ViewModel() {
  private val microService = MicroService()

  ///LIST USER
  var user by mutableStateOf<User?>(null)
  var getUserLoading by mutableStateOf(true)
  var userLimit by mutableIntStateOf(0)
  var existingUserPage by mutableIntStateOf(0)


  ///LIST USER POST
  private var post by mutableStateOf<UserPost?>(null)
  var filterPost by mutableStateOf<UserPost?>(null)
  var getPostLoading by mutableStateOf(true)
  var postLimit by mutableIntStateOf(0)
  var existingPostPage by mutableIntStateOf(0)

  //USER DETAIL & POST
  var userDetail by mutableStateOf<UserDetail?>(null)
  private var userPost by mutableStateOf<UserPost?>(null)
  var filterUserPost by mutableStateOf<UserPost?>(null)
  var getUserPostLoading by mutableStateOf(true)
  var getUserDetailLoading by mutableStateOf(true)
  var userPostLimit by mutableIntStateOf(0)
  var existingUserPostPage by mutableIntStateOf(0)

  //LIKE AND FRIEND
  var likeList by mutableStateOf<List<Likes>>(listOf())
  var filterLikeList by mutableStateOf<List<Likes>>(listOf())
  var filterLikeKey by mutableStateOf<List<String>>(listOf())
  var listFriend by mutableStateOf<List<Friend>>(listOf())
  var existingLikePage by mutableIntStateOf(0)


  var searchController by mutableStateOf("")
  var darkMode by mutableStateOf(false)

  fun changeTheme() {
    darkMode = !darkMode
  }

  fun init(context: Context) {
    MainModel.mainViewModel = this
    database = Room.databaseBuilder(
      context,
      AppDatabase::class.java, "LOGICQUE"
    ).allowMainThreadQueries().build()
    getUser()
    getPost()
    likeList = database.databaseDao().getAllLikes()
    filterLikeList = likeList
    listFriend = database.databaseDao().getAllFriend()
  }

  fun getUser() {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.getUsers(page = existingUserPage).collectLatest {
        when (it) {
          is CoreError -> {
            println(it.message)
          }

          is CoreException -> {
            println(it.e)
          }

          is CoreSuccess -> {
            user = it.data
            userLimit = it.data.total!!
            getUserLoading = false
          }

          CoreTimeout -> {
            println(it.toString())
          }

          Loading -> {
            getUserLoading = true
          }
        }
      }
    }
  }


  fun getUserDetail(id: String) {
    viewModelScope.launch(Dispatchers.IO) {
      searchController = ""
      microService.repository.getUserDetail(id).collectLatest {
        when (it) {
          is CoreError -> {}
          is CoreException -> {}
          is CoreSuccess -> {
            userDetail = it.data
            getUserDetailLoading = false
          }

          CoreTimeout -> {}
          Loading -> {
            getUserDetailLoading = true
          }
        }
      }
    }
  }

  fun getUserPost(id: String) {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.getUserPost(id = id, page = existingUserPostPage).collectLatest {
        when (it) {
          is CoreError -> {}
          is CoreException -> {}
          is CoreSuccess -> {
            userPost = it.data
            filterUserPost = it.data
            userPostLimit = it.data?.total!!
            getUserPostLoading = false
          }

          CoreTimeout -> {}
          Loading -> {
            getUserPostLoading = true
          }
        }
      }
    }
  }

  fun getPost() {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.getPost(page = existingPostPage).collectLatest {
        when (it) {
          is CoreError -> {}
          is CoreException -> {}
          is CoreSuccess -> {
            post = it.data
            postLimit = it.data.total!!
            filterPost = post
            getPostLoading = false
          }

          CoreTimeout -> {}
          Loading -> {
            getPostLoading = true
          }
        }
      }
    }
  }

  fun onSearchPostChange(text: String) {
    searchController = text
  }

  fun onSearchPost(context: Context) {
    val find =
      userPost?.data?.filter { it?.text?.contains(searchController, ignoreCase = true) ?: false }
    if (find?.isEmpty() == true) {
      filterUserPost = userPost
      Toast.makeText(context, "Not Found", Toast.LENGTH_SHORT).show()
      return
    }
    filterUserPost = filterUserPost?.copy(data = find)
    Toast.makeText(context, "Found ${find?.size} post", Toast.LENGTH_SHORT).show()
  }

  fun addFriend(userId: String?, data: UserDetail?) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        database.databaseDao().insertFriend(Friend(userId = "$userId", user = data))
        listFriend = database.databaseDao().getAllFriend()
      } catch (_: Exception) {
      }
    }
  }

  fun removeFriend(userId: String?) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        database.databaseDao().deleteFriend(Friend(userId = "$userId"))
        listFriend = database.databaseDao().getAllFriend()
      } catch (_: Exception) {
      }
    }
  }

  fun like(postId: String?, data: PostData?) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        database.databaseDao()
          .insertLike(Likes(uid = "$postId", postData = data))
        likeList = database.databaseDao().getAllLikes()
        filterLikeList = likeList
        filterLikeKey = listOf()
      } catch (_: Exception) {
      }
    }
  }

  fun unlike(postId: String?) {
    viewModelScope.launch(Dispatchers.IO) {
      database.databaseDao().unlike(Likes(uid = "$postId"))
      likeList = database.databaseDao().getAllLikes()
      filterLikeList = likeList
      filterLikeKey = listOf()
    }
  }

  fun filterLikes(key: String) {
    if (filterLikeKey.contains(key)) {
      val drop = filterLikeKey.filter { it != key }
      filterLikeKey = drop
    } else {
      filterLikeKey += key
    }
    val find =
      likeList.filter { it.postData?.tags?.any { a -> filterLikeKey.contains(a) } ?: false }
    if (find.isEmpty()) {
      filterLikeList = likeList
      return
    }
    filterLikeList = find
  }
}