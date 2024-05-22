package id.test.logicque.ui.custom

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size


@Composable
fun ShimmerBox(
  content: @Composable (() -> Unit),
  hasLoading: Boolean,
  modifier: Modifier,
  onClick: () -> Unit
) {
  if (hasLoading) {
    Box(
      modifier = modifier
        .padding(10.dp)
        .clip(shape = RoundedCornerShape(15.dp))
        .background(color = Color.LightGray)
        .shimmerLoadingAnimation()
        .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(15.dp))
    )
  } else {
    Box(
      modifier = modifier
        .clickable { onClick() }
        .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(15.dp))
        .clip(shape = RoundedCornerShape(15.dp))
        .padding(10.dp), contentAlignment = Alignment.Center
    ) {
      content()
    }
  }
}

fun Modifier.shimmerLoadingAnimation(
  widthOfShadowBrush: Int = 500,
  angleOfAxisY: Float = 270f,
  durationMillis: Int = 1000,
): Modifier {
  return composed {
    val shimmerColors = listOf(
      Color.White.copy(alpha = 0.3f),
      Color.White.copy(alpha = 0.5f),
      Color.White.copy(alpha = 1.0f),
      Color.White.copy(alpha = 0.5f),
      Color.White.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
      initialValue = 0f,
      targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
      animationSpec = infiniteRepeatable(
        animation = tween(
          durationMillis = durationMillis,
          easing = LinearEasing,
        ),
        repeatMode = RepeatMode.Restart,
      ),
      label = "Shimmer loading animation",
    )

    this.background(
      brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
      ),
    )
  }
}

@Composable
fun ImageLoad(
  url: Any, size: Dp = 200.dp
) {
  val context = LocalContext.current
  val imageLoader = ImageLoader.Builder(context).components {
    if (Build.VERSION.SDK_INT >= 28) {
      add(ImageDecoderDecoder.Factory())
    } else {
      add(GifDecoder.Factory())
    }
  }.build()
  Image(
    alignment = Alignment.Center,
    modifier = Modifier
      .fillMaxWidth()
      .size(size),
    painter = rememberAsyncImagePainter(
      ImageRequest.Builder(context).data(data = url).apply(block = {
        size(Size.ORIGINAL)
      }).build(), imageLoader = imageLoader
    ),
    contentDescription = null
  )
}
