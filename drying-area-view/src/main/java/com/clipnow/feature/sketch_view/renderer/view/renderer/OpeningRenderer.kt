package com.clipnow.feature.sketch_view.renderer.view.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import com.clipnow.feature.sketch_view.renderer.data.WallOpeningData
import com.clipnow.feature.sketch_view.renderer.dpToPx


class OpeningRenderer(context: Context) : ElementRenderer<WallOpeningData> {

    private val fillPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val strokePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = context.dpToPx(1f) // Толщина границы
        isAntiAlias = true
        pathEffect = DashPathEffect(floatArrayOf(context.dpToPx(2f), context.dpToPx(2f)), 0f)  // Настройка для пунктирной линии
    }

    private val rectanglePath = Path()

    override fun render(canvas: Canvas, element: WallOpeningData, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float) {
        // Конвертируем координаты прямоугольника открытия в координаты Canvas
        val rectStartX1 = toCanvasX(element.rectStartX1)
        val rectStartY1 = toCanvasY(element.rectStartY1)
        val rectEndX1 = toCanvasX(element.rectEndX1)
        val rectEndY1 = toCanvasY(element.rectEndY1)
        val rectStartX2 = toCanvasX(element.rectStartX2)
        val rectStartY2 = toCanvasY(element.rectStartY2)
        val rectEndX2 = toCanvasX(element.rectEndX2)
        val rectEndY2 = toCanvasY(element.rectEndY2)

        // Создаем Path для прямоугольника открытия
        with(rectanglePath){
            reset()
            moveTo(rectStartX1, rectStartY1)
            lineTo(rectEndX1, rectEndY1)
            lineTo(rectEndX2, rectEndY2)
            lineTo(rectStartX2, rectStartY2)
            close()
        }

        // Рисуем серый прямоугольник и черную границу
        canvas.drawPath(rectanglePath, fillPaint)
        canvas.drawPath(rectanglePath, strokePaint)
    }
}