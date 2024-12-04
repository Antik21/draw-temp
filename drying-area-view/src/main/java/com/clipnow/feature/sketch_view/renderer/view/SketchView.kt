package com.clipnow.feature.sketch_view.renderer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.clipnow.feature.sketch_view.renderer.data.RoomData
import com.clipnow.feature.sketch_view.renderer.dpToPx
import com.clipnow.feature.sketch_view.renderer.view.renderer.DoorRenderer
import com.clipnow.feature.sketch_view.renderer.view.renderer.DryingAreaRenderer
import com.clipnow.feature.sketch_view.renderer.view.renderer.LengthLineRenderer
import com.clipnow.feature.sketch_view.renderer.view.renderer.OpeningRenderer
import com.clipnow.feature.sketch_view.renderer.view.renderer.PerimeterRenderer
import com.clipnow.feature.sketch_view.renderer.view.renderer.WindowRenderer

class SketchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paddingRoom = context.dpToPx(32f)

    private var scaleX = 1f
    private var scaleY = 1f
    private var offsetX = 0f
    private var offsetY = 0f

    private var roomData: RoomData? = null

    private val borderPaint = Paint().apply {
        color = Color.parseColor("#BFD1D8")
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = context.dpToPx(1f) // Толщина линии 1dp
    }

    // Закругление 8dp для углов
    private val cornerRadius = context.dpToPx(8f)

    // Lazy initializations for each renderer
    private val perimeterRenderer by lazy { PerimeterRenderer(context) }
    private val windowRenderer by lazy { WindowRenderer(context) }
    private val openingRenderer by lazy { OpeningRenderer(context) }
    private val doorRenderer by lazy { DoorRenderer(context) }
    private val dryingAreaRenderer by lazy { DryingAreaRenderer(context) }
    private val lengthLineRenderer by lazy { LengthLineRenderer(context) }

    fun setRoomData(data: RoomData) {
        roomData = data
        calculateScaleAndOffset()
        invalidate()
    }

    private fun calculateScaleAndOffset() {
        roomData?.let { data ->
            val roomBounds = data.bounds

            // Учитываем масштабирование и отступы по горизонтали и вертикали
            scaleX = (measuredWidth - 2 * paddingRoom) / roomBounds.width()
            scaleY = (measuredHeight - 2 * paddingRoom) / roomBounds.height()
            val scale = minOf(scaleX, scaleY)

            // Устанавливаем одинаковый масштаб для обеих осей
            scaleX = scale
            scaleY = scale

            // Рассчитываем смещение для центрирования комнаты с учетом отступов
            offsetX = (measuredWidth - roomBounds.width() * scaleX) / 2 - roomBounds.left * scaleX
            offsetY = (measuredHeight - roomBounds.height() * scaleY) / 2 - roomBounds.top * scaleY
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateScaleAndOffset()
    }

    private fun toCanvasX(x: Float): Float = x * scaleX + offsetX
    private fun toCanvasY(y: Float): Float = y * scaleY + offsetY

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        roomData?.let { data ->
            // Render perimeter
            perimeterRenderer.render(canvas, data.perimeter, ::toCanvasX, ::toCanvasY)

            // Render drying area
            data.dryingArea.let { dryingAreaRenderer.render(canvas, it, ::toCanvasX, ::toCanvasY) }

            data.perimeter.walls.forEach { wall ->
                // Render windows
                wall.windows.forEach { windowRenderer.render(canvas, it, ::toCanvasX, ::toCanvasY) }

                // Render openings
                wall.openings.forEach { openingRenderer.render(canvas, it, ::toCanvasX, ::toCanvasY) }

                // Render doors
                wall.doors.forEach { doorRenderer.render(canvas, it, ::toCanvasX, ::toCanvasY) }

                // Render length lines
                wall.lengthLines.forEach { lengthLineRenderer.render(canvas, it, ::toCanvasX, ::toCanvasY)}
            }
        }

        drawBorder(canvas)
    }

    private fun drawBorder(canvas: Canvas){
        // Отступ для границы, чтобы она не обрезалась краями View
        val padding = borderPaint.strokeWidth / 2
        val borderRect = RectF(
            padding,
            padding,
            width.toFloat() - padding,
            height.toFloat() - padding
        )

        // Рисуем закруглённый прямоугольник вокруг RoomView
        canvas.drawRoundRect(borderRect, cornerRadius, cornerRadius, borderPaint)
    }
}
