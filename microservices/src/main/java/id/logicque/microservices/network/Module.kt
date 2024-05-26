package id.logicque.microservices.network

import id.logicque.microservices.data.User
import id.logicque.microservices.data.UserDetail
import id.logicque.microservices.data.userpost.UserPost
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

internal object Module {
  fun repo(): Services {
    val timeoutInSeconds = 30000L
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val http = OkHttpClient().newBuilder().addInterceptor(logging)
      .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
      .readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
      .writeTimeout(timeoutInSeconds, TimeUnit.SECONDS).build()
    return Retrofit.Builder().baseUrl(BASE_URL).client(http).addConverterFactory(
      GsonConverterFactory.create()
    ).build().create(Services::class.java)
  }
}

interface Services {
  @Headers("Content-Type: application/json; charset=utf-8", "app-id:664daa2b9aecd0ce8ada897c")
  @GET(GET_USERS)
  suspend fun getUser(@Query("page") page: Int, @Query("limit") limit: Int): Response<User>

  @Headers("Content-Type: application/json; charset=utf-8", "app-id:664daa2b9aecd0ce8ada897c")
  @GET(GET_DETAIL_USERS)
  suspend fun getDetailUser(@Path("id") id: String): Response<UserDetail>

  @Headers("Content-Type: application/json; charset=utf-8", "app-id:664daa2b9aecd0ce8ada897c")
  @GET(GET_USER_POST)
  suspend fun getUserPost(
    @Path("id") id: String,
    @Query("page") page: Int,
    @Query("limit") limit: Int
  ): Response<UserPost>

  @Headers("Content-Type: application/json; charset=utf-8", "app-id:664daa2b9aecd0ce8ada897c")
  @GET(GET_POST)
  suspend fun getPost(@Query("page") page: Int, @Query("limit") limit: Int): Response<UserPost>
}

