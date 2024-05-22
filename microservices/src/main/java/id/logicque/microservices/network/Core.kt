package id.logicque.microservices.network

sealed interface Core<out T>
class CoreSuccess<T>(val data: T) : Core<T>
class CoreError<T : Any>(val code: Int, val message: String?) : Core<T>
data object CoreTimeout : Core<Nothing>
class CoreException<T : Any>(val e: String) : Core<T>
data object Loading : Core<Nothing>
