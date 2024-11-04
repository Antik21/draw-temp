package com.clipnow.feature.drying_area_view.renderer.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PathEffect
import android.graphics.DashPathEffect
import com.clipnow.feature.drying_area_view.renderer.data.LengthLineData
import com.clipnow.feature.drying_area_view.renderer.dpToPx
import kotlin.math.pow
import kotlin.math.sqrt

class LengthLineRenderer(context: Context) : ElementRenderer<LengthLineData> {

    private val linePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = context.dpToPx(1f)
        isAntiAlias = true
    }

    private val dashPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = context.dpToPx(1f)
        isAntiAlias = true
        pathEffect = DashPathEffect(floatArrayOf(context.dpToPx(5f), context.dpToPx(3f)), 0f)  // Настройка для пунктирной линии
    }

    private val arrowPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = context.dpToPx(8f)
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val arrowSize = context.dpToPx(3f)
    private val padding = context.dpToPx(3f)

    override fun render(canvas: Canvas, element: LengthLineData, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float) {
        val startX = toCanvasX(element.startX)
        val startY = toCanvasY(element.startY)
        val endX = toCanvasX(element.endX)
        val endY = toCanvasY(element.endY)

        // Преобразуем координаты текста в координаты Canvas
        val textX = toCanvasX(element.textX)
        val textY = toCanvasY(element.textY)

        // Вычисляем ширину и высоту текста для создания настоящего разрыва
        val textWidth = textPaint.measureText(element.text) + padding * 2

        // Рассчитываем точки разрыва на линии
        val lineLength = sqrt((endX - startX).pow(2) + (endY - startY).pow(2))
        val halfGapLength = textWidth / 2
        val gapStartRatio = (lineLength / 2 - halfGapLength) / lineLength
        val gapEndRatio = (lineLength / 2 + halfGapLength) / lineLength

        // Координаты начала и конца линии с учётом разрыва
        val breakStartX = startX + (endX - startX) * gapStartRatio
        val breakStartY = startY + (endY - startY) * gapStartRatio
        val breakEndX = startX + (endX - startX) * gapEndRatio
        val breakEndY = startY + (endY - startY) * gapEndRatio

        // Рисуем первую часть линии до разрыва
        canvas.drawLine(startX, startY, breakStartX, breakStartY, linePaint)

        // Рисуем вторую часть линии после разрыва
        canvas.drawLine(breakEndX, breakEndY, endX, endY, linePaint)

        // Рисуем стрелки на концах линии
        drawArrow(canvas, startX, startY, element.textAngle + 180)
        drawArrow(canvas, endX, endY, element.textAngle)

        // Рисуем текст по центру разрыва
        canvas.save()
        canvas.rotate(element.textAngle, textX, textY)
        canvas.drawText(element.text, textX, textY - (textPaint.descent() + textPaint.ascent()) / 2, textPaint)
        canvas.restore()

        // Преобразование координат пунктирных линий
        val dashStartXCanvas = toCanvasX(element.dashStartX)
        val dashStartYCanvas = toCanvasY(element.dashStartY)
        val dashEndXCanvas = toCanvasX(element.dashEndX)
        val dashEndYCanvas = toCanvasY(element.dashEndY)

        // Рисуем пунктирные линии от точек на стене до начала и конца длины сегмента
        canvas.drawLine(dashStartXCanvas, dashStartYCanvas, startX, startY, dashPaint)
        canvas.drawLine(dashEndXCanvas, dashEndYCanvas, endX, endY, dashPaint)
    }

    private fun drawArrow(canvas: Canvas, x: Float, y: Float, angle: Float) {
        val path = android.graphics.Path()
        path.moveTo(x, y)
        path.lineTo(x - arrowSize, y - arrowSize / 2)
        path.lineTo(x - arrowSize, y + arrowSize / 2)
        path.close()

        canvas.save()
        canvas.rotate(angle, x, y)
        canvas.drawPath(path, arrowPaint)
        canvas.restore()
    }
}
