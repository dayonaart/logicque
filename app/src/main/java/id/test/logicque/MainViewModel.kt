package id.test.logicque

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.logicque.microservices.network.CoreError
import id.logicque.microservices.network.CoreException
import id.logicque.microservices.network.CoreSuccess
import id.logicque.microservices.network.CoreTimeout
import id.logicque.microservices.network.Loading
import id.logicque.microservices.MicroService
import id.logicque.microservices.data.DataItem
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.DataItems
import id.logicque.microservices.data.userpost.UserPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object MainModel {
  lateinit var mainViewModel: MainViewModel
}

class MainViewModel : ViewModel() {
  private val microService = MicroService()
  var user by mutableStateOf<List<DataItem?>>(listOf())
  var userDetail by mutableStateOf<UserDetail?>(null)
  var userPost by mutableStateOf<UserPost?>(null)
  var userPostFilter by mutableStateOf<UserPost?>(null)
  var postLikeList by mutableStateOf<List<DataItems?>>(listOf())
  var getUserLoading by mutableStateOf(true)
  var getUserDetailLoading by mutableStateOf(true)
  private var userLimit by mutableIntStateOf(0)
  private var page by mutableIntStateOf(0)
  var searchController by mutableStateOf("")
  var darkMode by mutableStateOf(false)

  fun changeTheme() {
    darkMode = !darkMode
  }

  fun init() {
    MainModel.mainViewModel = this
  }

  private fun getUser() {
    viewModelScope.launch(Dispatchers.IO) {
      if (user.size >= userLimit && user.isNotEmpty()) return@launch
      if (user.size >= 20) page++
      microService.repository.getUsers(page = page, limit = 20).collectLatest {
        when (it) {
          is CoreError -> {
            println(it.message)
          }

          is CoreException -> {
            println(it.e)
          }

          is CoreSuccess -> {
            user += it.data.data!!
            userLimit = it.data.total!!
            microService.repository.saveUserList(user)
            delay(1000)
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

  fun extendUser() {
    getUser()
  }

  fun getUserDetail(id: String) {
    viewModelScope.launch(Dispatchers.IO) {
      userPostFilter = null
      searchController = ""
      microService.repository.getUserDetail(id).collectLatest {
        when (it) {
          is CoreError -> {}
          is CoreException -> {}
          is CoreSuccess -> {
            val hasFriend = microService.repository.readFriendList()
              .find { f -> f?.firstName == it.data.firstName } != null
            userDetail = it.data.copy(hasFriend = hasFriend)
            getUserPost(id)
          }

          CoreTimeout -> {}
          Loading -> {
            getUserDetailLoading = true
          }
        }
      }
    }
  }

  fun onSearchPostChange(text: String) {
    searchController = text
  }

  fun onSearchPost(context: Context) {
    val filter =
      userPost?.data?.filter { it?.text?.startsWith(searchController, ignoreCase = true) ?: false }
    if (filter?.isEmpty() == true) {
      Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show()
      userPostFilter = null
      return
    }
    userPostFilter = userPost?.copy(data = filter)
  }

  fun addFriend(data: UserDetail?) {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.addFriend(data)
      userDetail = data?.copy(hasFriend = true)
    }
  }

  fun removeFriend(data: UserDetail?) {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.removeFriend(data)
      userDetail = data?.copy(hasFriend = false)
    }
  }

  private fun getUserPost(id: String) {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.getUserPost(id).collectLatest {
        when (it) {
          is CoreError -> {}
          is CoreException -> {}
          is CoreSuccess -> {
            val existingLike = microService.repository.readLikeUserPost()
            val like = it.data?.data?.map { d ->
              val i = existingLike.indexOf(d)
              if (i != -1) {
                existingLike[i]?.copy(hasLike = true)
              } else {
                d
              }
            }
            userPost = it.data?.copy(data = like)
            delay(1000)
            getUserDetailLoading = false
          }

          CoreTimeout -> {}
          Loading -> {
          }
        }
      }
    }
  }

  fun likePost(data: DataItems?) {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.likeUserPost(data)
      val like = userPost?.data?.map { d ->
        val compare = data == d
        if (compare) {
          d?.copy(hasLike = true, likes = (d.likes ?: 0) + 1)
        } else {
          d
        }
      }
      userPost = userPost?.copy(data = like)
    }
  }

  fun unlikePost(data: DataItems?) {
    viewModelScope.launch(Dispatchers.IO) {
      microService.repository.unLikeUserPost(data)
      val like = userPost?.data?.map { d ->
        val compare = data == d
        if (compare) {
          d?.copy(hasLike = false, likes = (d.likes ?: 0) - 1)
        } else {
          d
        }
      }
      userPost = userPost?.copy(data = like)
    }
  }

  suspend fun getPostLike() {
    postLikeList = microService.repository.readLikeUserPost()
  }
}