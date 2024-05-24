package id.test.logicque.ui.custom

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import kotlin.math.max


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
fun ShimmerVerticalGridView(
  modifier: Modifier,
  itemSize: Int = 0,
  shimmerSize: Dp,
  shimmerBackground: Color = Color.Gray
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier,
    contentPadding = PaddingValues(10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    items(itemSize) {
      Box(
        modifier = Modifier
          .border(
            width = 1.dp,
            shape = RoundedCornerShape(10),
            color = Color.Gray
          )
          .size(shimmerSize)
          .shimmer()
          .background(shimmerBackground, shape = RoundedCornerShape(10))
      )
    }
  }
}

@Composable
fun ShimmerVerticalView(
  itemsSize: Int = 0,
  shimmerSize: Dp,
) {
  (0..itemsSize).forEach { _ ->
    ShimmerBox(
      modifier = Modifier
        .padding(bottom = 10.dp)
        .fillMaxWidth()
        .height(shimmerSize)
    )
  }
}

@Composable
fun ShimmerImageRounded(
  shimmerSize: Dp,
  shimmerBackground: Color = Color.Gray
) {
  Box(
    modifier = Modifier
      .clip(CircleShape)
      .size(shimmerSize)
      .shimmer()
      .background(shimmerBackground, shape = RoundedCornerShape(10))
  )
}

@Composable
fun ShimmerBox(
  shimmerBackground: Color = Color.Gray,
  modifier: Modifier
) {
  Box(
    modifier = modifier
      .clip(RectangleShape)
      .shimmer()
      .background(shimmerBackground, shape = RoundedCornerShape(10))
  )
}

@Composable
fun Int.SpaceHeight() {
  Spacer(modifier = Modifier.height(this.dp))
}

@Composable
fun Int.SpaceWidth() {
  Spacer(modifier = Modifier.width(this.dp))
}

fun Modifier.verticalScrollbar(
  scrollState: ScrollState,
  scrollBarWidth: Dp = 4.dp,
  minScrollBarHeight: Dp = 5.dp,
  scrollBarColor: Color = Color.Blue,
  cornerRadius: Dp = 2.dp
): Modifier = composed {
  val targetAlpha = if (scrollState.isScrollInProgress) 1f else 0f
  val duration = if (scrollState.isScrollInProgress) 150 else 500

  val alpha by animateFloatAsState(
    targetValue = targetAlpha,
    animationSpec = tween(durationMillis = duration), label = ""
  )
  drawWithContent {
    drawContent()
    val needDrawScrollbar = scrollState.isScrollInProgress || alpha > 0.0f
    if (needDrawScrollbar && scrollState.maxValue > 0) {
      val visibleHeight: Float = this.size.height - scrollState.maxValue
      val scrollBarHeight: Float =
        max(visibleHeight * (visibleHeight / this.size.height), minScrollBarHeight.toPx())
      val scrollPercent: Float = scrollState.value.toFloat() / scrollState.maxValue
      val scrollBarOffsetY: Float =
        scrollState.value + (visibleHeight - scrollBarHeight) * scrollPercent

      drawRoundRect(
        color = scrollBarColor,
        topLeft = Offset(this.size.width - scrollBarWidth.toPx(), scrollBarOffsetY),
        size = Size(scrollBarWidth.toPx(), scrollBarHeight),
        alpha = alpha,
        cornerRadius = CornerRadius(cornerRadius.toPx())
      )
    }
  }
}

@Composable
fun Modifier.verticalGridScrollbar(
  state: LazyGridState,
  width: Dp = 8.dp
): Modifier {
  val targetAlpha = if (state.isScrollInProgress) 1f else 0f
  val duration = if (state.isScrollInProgress) 150 else 500

  val alpha by animateFloatAsState(
    targetValue = targetAlpha,
    animationSpec = tween(durationMillis = duration), label = ""
  )
  return drawWithContent {
    drawContent()
    val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
    val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

    if (needDrawScrollbar && firstVisibleElementIndex != null) {
      val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
      val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
      val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight
      drawRoundRect(
        color = Color.Red,
        topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
        size = Size(width.toPx(), scrollbarHeight),
        alpha = alpha,
        cornerRadius = CornerRadius(10.dp.toPx())
      )
    }
  }
}

fun LazyGridScope.sticky(
  content: @Composable LazyGridItemScope.() -> Unit
) {
  item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

fun LazyListScope.sticky(
  content: @Composable LazyItemScope.() -> Unit
) {
  item(content = { content() })
}
