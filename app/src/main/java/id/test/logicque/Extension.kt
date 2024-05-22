package id.test.logicque

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun String.dateFormatter(): String {
  val formatter = SimpleDateFormat("yyyy-MM-dd")
  val date = formatter.parse(this)
  if (date != null) {
    return formatter.format(date)
  }
  return ""
}