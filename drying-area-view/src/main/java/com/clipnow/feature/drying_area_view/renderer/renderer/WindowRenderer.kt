package com.clipnow.feature.drying_area_view.renderer.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.clipnow.feature.drying_area_view.renderer.data.WallWindowData


class WindowRenderer : ElementRenderer<WallWindowData> {

    private val paint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    override fun render(canvas: Canvas, element: WallWindowData, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float) {
        // Convert window coordinates to canvas coordinates
        val startX = toCanvasX(element.startX)
        val startY = toCanvasY(element.startY)
        val endX = toCanvasX(element.endX)
        val endY = toCanvasY(element.endY)

        // Draw the window line along the wall
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}