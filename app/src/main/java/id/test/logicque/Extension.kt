package id.test.logicque

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun String.dateFormatter(): String {
  val formatter = SimpleDateFormat("yyyy-MM-dd")
  val date = formatter.parse(this)
  if (date != null) {
    return formatter.format(date)
  }
  return ""
}

fun String.capitalizes(): String {
  return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}
