package id.logicque.microservices.repository

import id.logicque.microservices.data.User
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.UserPost
import id.logicque.microservices.network.Core
import kotlinx.coroutines.flow.Flow

interface Repository {
  fun getUsers(page: Int = 0, limit: Int = 20): Flow<Core<User>>
  fun getPost(page: Int = 0, limit: Int = 20): Flow<Core<UserPost>>
  fun getUserDetail(id: String): Flow<Core<UserDetail>>
  fun getUserPost(id: String, page: Int = 0, limit: Int = 20): Flow<Core<UserPost?>>
}