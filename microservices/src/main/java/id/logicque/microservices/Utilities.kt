package id.logicque.microservices

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.logicque.microservices.data.DataItem
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.PostData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter

internal object Utilities {
  private val EXTERNAL_FILE_DB = File(
    "/storage/emulated/0/Android/data/id.test.logicque/files/database/user.db"
  )
  private val EXTERNAL_FILE_FRIENDS_DB = File(
    "/storage/emulated/0/Android/data/id.test.logicque/files/database/friends.db"
  )
  private val EXTERNAL_FILE_LIKE_DB = File(
    "/storage/emulated/0/Android/data/id.test.logicque/files/database/likes.db"
  )

  internal suspend fun saveUserList(data: String) {
    try {
      val writer = withContext(Dispatchers.IO) {
        FileWriter(EXTERNAL_FILE_DB)
      }
      withContext(Dispatchers.IO) {
        writer.write(data)
      }
      withContext(Dispatchers.IO) {
        writer.close()
      }
      println("SAVED USER")
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  internal fun readUserList(): List<DataItem?> {
    return try {
      val data: List<DataItem?> = Gson().fromJson(
        EXTERNAL_FILE_DB.readText(), object : TypeToken<List<DataItem?>>() {}.type
      )
      data.filterNotNull().toSet().toList()
    } catch (e: Exception) {
      listOf()
    }
  }

  internal suspend fun addFriend(data: List<UserDetail?>) {
    try {
      val writer = withContext(Dispatchers.IO) {
        FileWriter(EXTERNAL_FILE_FRIENDS_DB)
      }
      withContext(Dispatchers.IO) {
        writer.write(Gson().toJson(data))
      }
      withContext(Dispatchers.IO) {
        writer.close()
      }
      println("SAVED FRIEND")
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  internal fun readFriendList(): List<UserDetail?> {
    return try {
      val data: List<UserDetail?> = Gson().fromJson(
        EXTERNAL_FILE_FRIENDS_DB.readText(), object : TypeToken<List<UserDetail?>>() {}.type
      )
      data.filterNotNull().toSet().toList()
    } catch (e: Exception) {
      listOf()
    }
  }


  internal suspend fun saveLikePost(data: List<PostData?>) {
    try {
      val writer = withContext(Dispatchers.IO) {
        FileWriter(EXTERNAL_FILE_LIKE_DB)
      }
      withContext(Dispatchers.IO) {
        writer.write(Gson().toJson(data))
      }
      withContext(Dispatchers.IO) {
        writer.close()
      }
      println("SAVED LIKE")
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  internal fun readLikePost(): List<PostData?> {
    return try {
      val data: List<PostData?> = Gson().fromJson(
        EXTERNAL_FILE_LIKE_DB.readText(), object : TypeToken<List<PostData?>>() {}.type
      )
      data.filterNotNull().toSet().toList()
    } catch (e: Exception) {
      listOf()
    }
  }
}
