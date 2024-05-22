package id.logicque.microservices

import java.util.Locale

fun String.capitalizes(): String {
  return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}