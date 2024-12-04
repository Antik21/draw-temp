package com.clipnow.feature.sketch_view.renderer.view.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.clipnow.feature.sketch_view.renderer.data.PerimeterData
import com.clipnow.feature.sketch_view.renderer.dpToPx


class PerimeterRenderer(private val context: Context) : ElementRenderer<PerimeterData> {
    private val wallPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val path = Path()

    override fun render(
        canvas: Canvas,
        element: PerimeterData,
        toCanvasX: (Float) -> Float,
        toCanvasY: (Float) -> Float
    ) {
        val wallWidth = context.dpToPx(element.wallThickness)
        wallPaint.strokeWidth = wallWidth

        path.reset()

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