package id.test.logicque.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import com.google.gson.Gson
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.PostData

@Dao
interface DatabaseDao {
  @Insert(onConflict = OnConflictStrategy.ABORT)
  fun insertFriend(friend: Friend)

  @Delete
  fun deleteFriend(friend: Friend)

  @Query("SELECT * FROM friend")
  fun getAllFriend(): List<Friend>

  @Query("SELECT * FROM friend WHERE userId LIKE :userId LIMIT 1")
  fun findFriendById(userId: String): Friend?

  @Insert(onConflict = OnConflictStrategy.ABORT)
  fun insertLike(likes: Likes)

  @Delete
  fun unlike(likes: Likes)

  @Query("SELECT * FROM likes")
  fun getAllLikes(): List<Likes>

  @Query("SELECT * FROM likes WHERE uid LIKE :postIds LIMIT 1")
  fun findLikeById(postIds: String): Likes?


//
//  @Query("SELECT * FROM user")
//  fun getAll(): List<User>
//
//  @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//  fun loadAllByIds(userIds: IntArray): List<User>
//
//  @Query(
//    "SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1"
//  )
//  fun findByName(first: String, last: String): User
//
//  @Insert
//  fun insertAll(vararg users: User)
//
//  @Delete
//  fun delete(user: User)
}


@Entity
data class Friend(
  @PrimaryKey val userId: String,
  @ColumnInfo(name = "user") val user: UserDetail? = null
)

@Entity
data class Likes(
  @PrimaryKey val uid: String,
  @ColumnInfo(name = "like_item") val postData: PostData? = null,
)

class TypeConverter {
  @TypeConverter
  fun fromUserModel(data: UserDetail?): String? = Gson().toJson(data)

  @TypeConverter
  fun toUserModel(data: String?): UserDetail? = Gson().fromJson(data, UserDetail::class.java)

  @TypeConverter
  fun fromLikeModel(data: PostData?): String? = Gson().toJson(data)

  @TypeConverter
  fun toLikeModel(data: String?): PostData? = Gson().fromJson(data, PostData::class.java)

}