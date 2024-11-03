package com.clipnow.feature.drying_area_view.renderer.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.clipnow.feature.drying_area_view.renderer.data.WallOpeningData


class OpeningRenderer : ElementRenderer<WallOpeningData> {

    private val paint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    override fun render(canvas: Canvas, element: WallOpeningData, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float) {
        // Convert opening coordinates to canvas coordinates
        val startX = toCanvasX(element.startX)
        val startY = toCanvasY(element.startY)
        val endX = toCanvasX(element.endX)
        val endY = toCanvasY(element.endY)

        // Draw the opening line along the wall
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}