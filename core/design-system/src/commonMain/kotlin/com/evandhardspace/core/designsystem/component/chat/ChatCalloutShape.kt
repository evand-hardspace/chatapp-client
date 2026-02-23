package com.evandhardspace.core.designsystem.component.chat

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

enum class CalloutPosition {
    Start,
    End,
}

class ChatBubbleShape(
    private val calloutPosition: CalloutPosition,
    private val tailSize: Dp,
    private val cornerRadius: Dp,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {

        val tailSizePx = with(density) { tailSize.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }

        val isTailOnLeft = when (calloutPosition) {
            CalloutPosition.Start -> layoutDirection == LayoutDirection.Ltr
            CalloutPosition.End -> layoutDirection == LayoutDirection.Rtl
        }

        val path = if (isTailOnLeft) createLeftBubble(size, tailSizePx, cornerRadiusPx)
        else createRightBubble(size, tailSizePx, cornerRadiusPx)

        return Outline.Generic(path)
    }

    private fun createLeftBubble(
        size: Size,
        tailSizePx: Float,
        cornerRadiusPx: Float,
    ): Path {
        val bodyPath = Path().apply {
            addRoundRect(
                RoundRect(
                    left = tailSizePx,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                )
            )
        }

        val tailPath = Path().apply {
            moveTo(0f, size.height)
            lineTo(tailSizePx, size.height - cornerRadiusPx)
            lineTo(tailSizePx + cornerRadiusPx, size.height)
            close()
        }

        return Path.combine(
            PathOperation.Union, 
            bodyPath, 
            tailPath,
        )
    }

    private fun createRightBubble(
        size: Size,
        tailSizePx: Float,
        cornerRadiusPx: Float,
    ): Path {
        val bodyPath = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width - tailSizePx,
                    bottom = size.height,
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                )
            )
        }

        val tailPath = Path().apply {
            moveTo(size.width, size.height)
            lineTo(size.width - tailSizePx, size.height - cornerRadiusPx)
            lineTo(size.width - tailSizePx - cornerRadiusPx, size.height)
            close()
        }

        return Path.combine(
            PathOperation.Union, 
            bodyPath, 
            tailPath,
        )
    }
}
