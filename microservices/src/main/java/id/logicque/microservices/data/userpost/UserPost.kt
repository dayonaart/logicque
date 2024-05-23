package id.logicque.microservices.data.userpost

import com.google.gson.annotations.SerializedName

data class UserPost(

  @field:SerializedName("total") val total: Int? = null,

  @field:SerializedName("data") val data: List<PostData?>? = null,

  @field:SerializedName("limit") val limit: Int? = null,

  @field:SerializedName("page") val page: Int? = null,
)

data class PostData(

  @field:SerializedName("owner") val owner: Owner? = null,

  @field:SerializedName("image") val image: String? = null,

  @field:SerializedName("publishDate") val publishDate: String? = null,

  @field:SerializedName("id") val id: String? = null,

  @field:SerializedName("text") val text: String? = null,

  @field:SerializedName("likes") val likes: Int? = null,

  @field:SerializedName("tags") val tags: List<String?>? = null,
)

data class Owner(

  @field:SerializedName("firstName") val firstName: String? = null,

  @field:SerializedName("lastName") val lastName: String? = null,

  @field:SerializedName("id") val id: String? = null,

  @field:SerializedName("title") val title: String? = null,

  @field:SerializedName("picture") val picture: String? = null
)
