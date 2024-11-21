package com.clipnow.feature.drying_area_view.renderer.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.clipnow.feature.drying_area_view.renderer.data.WallWindowData
import com.clipnow.feature.drying_area_view.renderer.dpToPx


class WindowRenderer(context: Context) : ElementRenderer<WallWindowData> {

    private val fillPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = context.dpToPx(1f)
        isAntiAlias = true
    }

    override fun render(canvas: Canvas, element: WallWindowData, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float) {
        // Конвертируем координаты прямоугольника окна в координаты Canvas
        val rectStartX1 = toCanvasX(element.rectStartX1)
        val rectStartY1 = toCanvasY(element.rectStartY1)
        val rectEndX1 = toCanvasX(element.rectEndX1)
        val rectEndY1 = toCanvasY(element.rectEndY1)
        val rectStartX2 = toCanvasX(element.rectStartX2)
        val rectStartY2 = toCanvasY(element.rectStartY2)
        val rectEndX2 = toCanvasX(element.rectEndX2)
        val rectEndY2 = toCanvasY(element.rectEndY2)

        // Создаем Path для прямоугольника открытия
        val path = Path().apply {
            moveTo(rectStartX1, rectStartY1)
            lineTo(rectEndX1, rectEndY1)
            lineTo(rectEndX2, rectEndY2)
            lineTo(rectStartX2, rectStartY2)
            close()
        }

        // Рисуем серый прямоугольник и черную границу
        canvas.drawPath(path, fillPaint)
        canvas.drawPath(path, linePaint)

        // Рисуем центральную линию окна
        val centerStartX = toCanvasX(element.centerStartX)
        val centerStartY = toCanvasY(element.centerStartY)
        val centerEndX = toCanvasX(element.centerEndX)
        val centerEndY = toCanvasY(element.centerEndY)

        canvas.drawLine(centerStartX, centerStartY, centerEndX, centerEndY, linePaint)
    }
}