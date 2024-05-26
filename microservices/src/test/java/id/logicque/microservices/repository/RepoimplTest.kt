package id.logicque.microservices.repository

import id.logicque.microservices.network.Module
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RepoimplTest {
  @Test
  fun `get user list`() {
    runBlocking {
      try {
        val res = Module.repo().getUser(0, 20)
        assert(res.isSuccessful)
      } catch (e: SocketTimeoutException) {
        println(e.message)
      } catch (e: SocketException) {
        println(e.message)
      } catch (e: UnknownHostException) {
        println(e.message)
      } catch (e: IOException) {
        println(e.message)
      } catch (e: HttpException) {
        println(e.message)
      } catch (e: Exception) {
        println(e.message)
      }
    }
  }

  @Test
  fun `get user post list`() {
    runBlocking {
      try {
        val res = Module.repo().getPost(0, 20)
        assert(res.isSuccessful)
      } catch (e: SocketTimeoutException) {
        println(e.message)
      } catch (e: SocketException) {
        println(e.message)
      } catch (e: UnknownHostException) {
        println(e.message)
      } catch (e: IOException) {
        println(e.message)
      } catch (e: HttpException) {
        println(e.message)
      } catch (e: Exception) {
        println(e.message)
      }
    }
  }

  @Test
  fun `get user detail`() {
    runBlocking {
      try {
        val res = Module.repo().getDetailUser("60d0fe4f5311236168a109e6")
        assert(res.isSuccessful)
      } catch (e: SocketTimeoutException) {
        println(e.message)
      } catch (e: SocketException) {
        println(e.message)
      } catch (e: UnknownHostException) {
        println(e.message)
      } catch (e: IOException) {
        println(e.message)
      } catch (e: HttpException) {
        println(e.message)
      } catch (e: Exception) {
        println(e.message)
      }
    }
  }

  @Test
  fun `get detail user post`() {
    runBlocking {
      try {
        val res = Module.repo().getUserPost(id = "60d0fe4f5311236168a109e6", page = 0, limit = 20)
        assert(res.isSuccessful)
      } catch (e: SocketTimeoutException) {
        println(e.message)
      } catch (e: SocketException) {
        println(e.message)
      } catch (e: UnknownHostException) {
        println(e.message)
      } catch (e: IOException) {
        println(e.message)
      } catch (e: HttpException) {
        println(e.message)
      } catch (e: Exception) {
        println(e.message)
      }
    }
  }
}