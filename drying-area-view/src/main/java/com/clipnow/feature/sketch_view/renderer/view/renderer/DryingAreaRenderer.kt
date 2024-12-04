package com.clipnow.feature.sketch_view.renderer.view.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.clipnow.feature.sketch_view.renderer.data.DryingAreaData
import com.clipnow.feature.sketch_view.renderer.dpToPx


class DryingAreaRenderer(context: Context) : ElementRenderer<DryingAreaData> {

    // Paint for the text displaying the area size
    private val textPaint = Paint().apply {
        color = Color.BLACK
        isFakeBoldText = true
        textSize = context.dpToPx(16f)
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun render(canvas: Canvas, element: DryingAreaData, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float) {
        // Convert the center coordinates to canvas coordinates
        val centerX = toCanvasX(element.centerX)
        val centerY = toCanvasY(element.centerY)

        // Draw the area text at the center position
        canvas.drawText(element.areaText, centerX, centerY, textPaint)
    }
}