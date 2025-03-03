package com.clipnow.feature.sketch_view.renderer.view.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.clipnow.feature.sketch_view.renderer.data.WallDoorData
import com.clipnow.feature.sketch_view.renderer.dpToPx

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
        strokeWidth = context.dpToPx(1f)
        isAntiAlias = true
    }

    private val doorPath = Path()
    private val doorArcPath = Path()
    private val doorArcRect = RectF()

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

        // Начало и радиус дуги для отображения направления открытия двери
        val arcStartX = toCanvasX(element.arcStartX)
        val arcStartY = toCanvasY(element.arcStartY)
        val arcRadius = toCanvasX(element.arcRadius) - toCanvasX(0f)

        doorArcRect.set(
            arcStartX - arcRadius,
            arcStartY - arcRadius,
            arcStartX + arcRadius,
            arcStartY + arcRadius
        )
        with(doorArcPath) {
            reset()
            moveTo(rectStartX1, rectStartY1)
            arcTo(doorArcRect, element.angleDegrees + 90, -90f)
        }
        canvas.drawPath(doorArcPath, arcPaint)

        // Рисуем серый прямоугольник двери
        with(doorPath) {
            reset()
            moveTo(rectStartX1, rectStartY1)
            lineTo(rectEndX1, rectEndY1)
            lineTo(rectEndX2, rectEndY2)
            lineTo(rectStartX2, rectStartY2)
            close()
        }
        canvas.drawPath(doorPath, doorPaintFill)
        canvas.drawPath(doorPath, doorPaintStroke)
    }
}
