package com.clipnow.feature.sketch_view.renderer.compose

import android.graphics.RectF
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

data class ScaleAndOffset(
    val scaleX: Float,
    val scaleY: Float,
    val offsetX: Float,
    val offsetY: Float
)
fun calculateScaleAndOffset(
    bounds: RectF,
    canvasWidth: Float,
    canvasHeight: Float,
    padding: Float
): ScaleAndOffset {
    // Calculate scaling factors
    val scaleX = (canvasWidth - 2 * padding) / bounds.width()
    val scaleY = (canvasHeight - 2 * padding) / bounds.height()
    val scale = minOf(scaleX, scaleY)

    // Apply uniform scale to both axes
    val adjustedScaleX = scale
    val adjustedScaleY = scale

    // Calculate offsets for centering the room
    val offsetX = (canvasWidth - bounds.width() * adjustedScaleX) / 2 - bounds.left * adjustedScaleX
    val offsetY = (canvasHeight - bounds.height() * adjustedScaleY) / 2 - bounds.top * adjustedScaleY

    return ScaleAndOffset(adjustedScaleX, adjustedScaleY, offsetX, offsetY)
}

fun Dp.toPx(density: Density): Float {
    return this.value * density.density
}

fun Float.toCanvasX(scaleAndOffset: ScaleAndOffset): Float =
    this * scaleAndOffset.scaleX + scaleAndOffset.offsetX

fun Float.toCanvasY(scaleAndOffset: ScaleAndOffset): Float =
    this * scaleAndOffset.scaleY + scaleAndOffset.offsetY