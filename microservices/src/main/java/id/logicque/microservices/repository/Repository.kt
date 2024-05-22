package id.logicque.microservices.repository

import id.logicque.microservices.data.DataItem
import id.logicque.microservices.network.Core
import id.logicque.microservices.data.User
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.DataItems
import id.logicque.microservices.data.userpost.UserPost
import kotlinx.coroutines.flow.Flow

interface Repository {
  fun getUsers(page: Int = 0, limit: Int = 50): Flow<Core<User>>
  fun getUserDetail(id: String): Flow<Core<UserDetail>>
  suspend fun saveUserList(data: List<DataItem?>)
  fun readUserList(): List<DataItem?>
  suspend fun addFriend(data: UserDetail?)
  suspend fun removeFriend(data: UserDetail?)
  suspend fun readFriendList(): List<UserDetail?>
  fun getUserPost(id: String): Flow<Core<UserPost?>>
  suspend fun likeUserPost(data: DataItems?)
  suspend fun unLikeUserPost(data: DataItems?)
  suspend fun readLikeUserPost(): List<DataItems?>
}