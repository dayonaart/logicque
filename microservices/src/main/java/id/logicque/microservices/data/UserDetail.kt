package id.logicque.microservices.data

import com.google.gson.annotations.SerializedName
import id.logicque.microservices.Utilities

data class UserDetail(

  @field:SerializedName("firstName") val firstName: String? = null,

  @field:SerializedName("lastName") val lastName: String? = null,

  @field:SerializedName("gender") val gender: String? = null,

  @field:SerializedName("phone") val phone: String? = null,

  @field:SerializedName("dateOfBirth") val dateOfBirth: String? = null,

  @field:SerializedName("location") val location: Location? = null,

  @field:SerializedName("id") val id: String? = null,

  @field:SerializedName("updatedDate") val updatedDate: String? = null,

  @field:SerializedName("title") val title: String? = null,

  @field:SerializedName("picture") val picture: String? = null,

  @field:SerializedName("email") val email: String? = null,

  @field:SerializedName("registerDate") val registerDate: String? = null,

  @field:SerializedName("hasFriend") var hasFriend: Boolean = false,
) {
  fun locationToString(): String {
    return "${location?.country}, ${location?.city}, ${location?.state}, ${location?.street}, "
  }
}

data class Location(

  @field:SerializedName("country") val country: String? = null,

  @field:SerializedName("city") val city: String? = null,

  @field:SerializedName("street") val street: String? = null,

  @field:SerializedName("timezone") val timezone: String? = null,

  @field:SerializedName("state") val state: String? = null
)
