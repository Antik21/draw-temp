package com.clipnow.feature.drying_area_view.renderer.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.clipnow.feature.drying_area_view.renderer.data.PerimeterData
import com.clipnow.feature.drying_area_view.renderer.data.WallData
import com.clipnow.feature.drying_area_view.renderer.dpToPx
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class PerimeterRenderer(private val context: Context) : ElementRenderer<PerimeterData> {
    private val wallPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = context.dpToPx(8f)
    }

    // Reusable Path instance
    private val path = Path()

    override fun render(
        canvas: Canvas,
        element: PerimeterData,
        toCanvasX: (Float) -> Float,
        toCanvasY: (Float) -> Float
    ) {
        // Reset the path for a new rendering cycle
        path.reset()

        val wallWidth = context.dpToPx(element.wallThickness)
        wallPaint.strokeWidth = wallWidth

        if (element.walls.isNotEmpty()) {
            val firstWall = element.walls[0]
            val startX = toCanvasX(firstWall.startX)
            val startY = toCanvasY(firstWall.startY)

            path.moveTo(startX, startY)

            element.walls.forEach { wall ->
                val endX = toCanvasX(wall.endX)
                val endY = toCanvasY(wall.endY)

                // Draw the wall line
                path.lineTo(endX, endY)
            }

            path.close()
            canvas.drawPath(path, wallPaint)
        }
    }
}