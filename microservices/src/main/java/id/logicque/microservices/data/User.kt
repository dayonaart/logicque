package id.logicque.microservices.data

import com.google.gson.annotations.SerializedName

data class User(

  @field:SerializedName("total")
  val total: Int? = null,

  @field:SerializedName("data")
  val data: List<DataItem?>? = null,

  @field:SerializedName("limit")
  val limit: Int? = null,

  @field:SerializedName("page")
  val page: Int? = null
)

data class DataItem(

  @field:SerializedName("firstName")
  val firstName: String? = null,

  @field:SerializedName("lastName")
  val lastName: String? = null,

  @field:SerializedName("id")
  val id: String? = null,

  @field:SerializedName("title")
  val title: String? = null,

  @field:SerializedName("picture")
  val picture: String? = null
)
