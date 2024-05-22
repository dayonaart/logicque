package id.logicque.microservices.repository

import com.google.gson.Gson
import id.logicque.microservices.Utilities
import id.logicque.microservices.data.DataItem
import id.logicque.microservices.network.Core
import id.logicque.microservices.network.CoreError
import id.logicque.microservices.network.CoreException
import id.logicque.microservices.network.CoreSuccess
import id.logicque.microservices.network.CoreTimeout
import id.logicque.microservices.network.Loading
import id.logicque.microservices.data.User
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.DataItems
import id.logicque.microservices.data.userpost.UserPost
import id.logicque.microservices.network.Module
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Repoimpl : Repository {
  override fun getUsers(page: Int, limit: Int): Flow<Core<User>> {
    return flow {
      try {
        emit(Loading)
        val res = Module.repo().getUser(page = page, limit = limit)
        if (!res.isSuccessful) {
          emit(CoreError(message = res.message(), code = res.code()))
          return@flow
        }
        emit(CoreSuccess(data = res.body()!!))
      } catch (e: SocketTimeoutException) {
        emit(CoreTimeout)
      } catch (e: SocketException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: UnknownHostException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: IOException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: HttpException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: Exception) {
        emit(CoreException(e = "${e.message}"))
      }
    }.flowOn(Dispatchers.IO)
  }

  override fun getUserDetail(id: String): Flow<Core<UserDetail>> {
    return flow {
      try {
        emit(Loading)
        val res = Module.repo().getDetailUser(id)
        if (!res.isSuccessful) {
          emit(CoreError(message = res.message(), code = res.code()))
          return@flow
        }
        emit(CoreSuccess(data = res.body()!!))
      } catch (e: SocketTimeoutException) {
        emit(CoreTimeout)
      } catch (e: SocketException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: UnknownHostException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: IOException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: HttpException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: Exception) {
        emit(CoreException(e = "${e.message}"))
      }
    }.flowOn(Dispatchers.IO)
  }

  override suspend fun saveUserList(data: List<DataItem?>) {
    Utilities.saveUserList(Gson().toJson(data))
  }

  override fun readUserList(): List<DataItem?> {
    return Utilities.readUserList()
  }

  override suspend fun addFriend(data: UserDetail?) {
    val existing = Utilities.readFriendList().toMutableList()
    existing += data
    Utilities.addFriend(existing)
    println("ADDED ${data?.firstName}")
  }

  override suspend fun removeFriend(data: UserDetail?) {
    val existing = Utilities.readFriendList().toMutableList()
    val removed = existing.filter { it?.firstName != data?.firstName }
    Utilities.addFriend(removed)
    println("REMOVED ${removed.map { data?.firstName }}")
  }

  override suspend fun readFriendList(): List<UserDetail?> {
    return Utilities.readFriendList()
  }

  override fun getUserPost(id: String): Flow<Core<UserPost?>> {
    return flow {
      try {
        emit(Loading)
        val res = Module.repo().getUserPost(id)
        if (!res.isSuccessful) {
          emit(CoreError(message = res.message(), code = res.code()))
          return@flow
        }
        emit(CoreSuccess(data = res.body()))
      } catch (e: SocketTimeoutException) {
        emit(CoreTimeout)
      } catch (e: SocketException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: UnknownHostException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: IOException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: HttpException) {
        emit(CoreException(e = "${e.message}"))
      } catch (e: Exception) {
        emit(CoreException(e = "${e.message}"))
      }
    }
  }

  override suspend fun likeUserPost(data: DataItems?) {
    val existing = Utilities.readLikePost().toMutableList()
    existing += data
    Utilities.saveLikePost(existing)
  }

  override suspend fun unLikeUserPost(data: DataItems?) {
    val existing = Utilities.readLikePost().toMutableList()
    val removed = existing.filter { it?.id != data?.id }
    Utilities.saveLikePost(removed)
  }

  override suspend fun readLikeUserPost(): List<DataItems?> {
    return Utilities.readLikePost()
  }
}