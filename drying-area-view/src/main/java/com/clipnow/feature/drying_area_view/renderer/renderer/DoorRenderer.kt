package com.clipnow.feature.drying_area_view.renderer.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.clipnow.feature.drying_area_view.renderer.data.WallDoorData
import com.clipnow.feature.drying_area_view.renderer.dpToPx
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class DoorRenderer(context: Context) : ElementRenderer<WallDoorData> {

    private val doorPaintFill = Paint().apply {
        color = Color.parseColor("#8E95A3")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val doorPaintStroke = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = context.dpToPx(1f) // Толщина границы двери
        isAntiAlias = true
    }

    private val arcPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f // Толщина дуги
        isAntiAlias = true
    }

    private val doorArcPath = Path()

    override fun render(
        canvas: Canvas,
        element: WallDoorData,
        toCanvasX: (Float) -> Float,
        toCanvasY: (Float) -> Float
    ) {
        // Конвертируем координаты углов прямоугольника двери в координаты Canvas
        val rectStartX1 = toCanvasX(element.rectStartX1)
        val rectStartY1 = toCanvasY(element.rectStartY1)
        val rectEndX1 = toCanvasX(element.rectEndX1)
        val rectEndY1 = toCanvasY(element.rectEndY1)
        val rectStartX2 = toCanvasX(element.rectStartX2)
        val rectStartY2 = toCanvasY(element.rectStartY2)
        val rectEndX2 = toCanvasX(element.rectEndX2)
        val rectEndY2 = toCanvasY(element.rectEndY2)

        // Рисуем серый прямоугольник двери
        val doorPath = Path().apply {
            moveTo(rectStartX1, rectStartY1)
            lineTo(rectEndX1, rectEndY1)
            lineTo(rectEndX2, rectEndY2)
            lineTo(rectStartX2, rectStartY2)
            close()
        }
        canvas.drawPath(doorPath, doorPaintFill)
        canvas.drawPath(doorPath, doorPaintStroke)

        // Дуга для направления открытия двери
        val arcStartX = toCanvasX(element.arcStartX)
        val arcStartY = toCanvasY(element.arcStartY)

        // Создаем Path для дуги
        with(doorArcPath) {
            reset()
            moveTo(rectEndX1, rectEndY1)
            arcTo(
                arcStartX - element.arcRadius,
                arcStartY - element.arcRadius,
                arcStartX + element.arcRadius,
                arcStartY + element.arcRadius,
                element.angleDegrees,
                90f,
                false
            )
        }

        // Рисуем дугу
        canvas.drawPath(doorArcPath, arcPaint)
    }
}
