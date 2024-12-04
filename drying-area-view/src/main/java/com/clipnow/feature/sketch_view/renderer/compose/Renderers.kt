package com.clipnow.feature.sketch_view.renderer.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextPainter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clipnow.feature.sketch_view.renderer.data.DryingAreaData
import com.clipnow.feature.sketch_view.renderer.data.PerimeterData
import com.clipnow.feature.sketch_view.renderer.data.WallDoorData
import com.clipnow.feature.sketch_view.renderer.data.WallOpeningData
import com.clipnow.feature.sketch_view.renderer.data.WallWindowData


fun DrawScope.renderPerimeter(perimeterData: PerimeterData, scaleAndOffset: ScaleAndOffset) {
    val wallPaint = Paint().apply {
        color = Color.Black
        style = PaintingStyle.Stroke
        isAntiAlias = true
        strokeWidth = perimeterData.wallThickness.dp.value
    }

    val firstWall = perimeterData.walls.first()
    val startX = firstWall.startX.toCanvasX(scaleAndOffset)
    val startY = firstWall.startY.toCanvasY(scaleAndOffset)

    val path = Path()
    path.moveTo(startX, startY)

    perimeterData.walls.forEach { wall ->
        val endX = wall.endX.toCanvasX(scaleAndOffset)
        val endY = wall.endY.toCanvasY(scaleAndOffset)
        path.lineTo(endX, endY)
    }

    path.close()
    drawContext.canvas.drawPath(path, wallPaint)
}

fun DrawScope.renderWindow(windowData: WallWindowData, scaleAndOffset: ScaleAndOffset) {
    // Paint for the window fill
    val fillPaint = Paint().apply {
        color = Color.White
        style = PaintingStyle.Fill
        isAntiAlias = true
    }

    // Paint for the window outline and center line
    val linePaint = Paint().apply {
        color = Color.Black
        style = PaintingStyle.Stroke
        strokeWidth = 1.dp.toPx(this@renderWindow)
        isAntiAlias = true
    }

    val rectanglePath = Path()

    // Convert coordinates for the outer rectangle of the window
    val rectStartX1 = windowData.rectStartX1.toCanvasX(scaleAndOffset)
    val rectStartY1 = windowData.rectStartY1.toCanvasY(scaleAndOffset)
    val rectEndX1 = windowData.rectEndX1.toCanvasX(scaleAndOffset)
    val rectEndY1 = windowData.rectEndY1.toCanvasY(scaleAndOffset)
    val rectStartX2 = windowData.rectStartX2.toCanvasX(scaleAndOffset)
    val rectStartY2 = windowData.rectStartY2.toCanvasY(scaleAndOffset)
    val rectEndX2 = windowData.rectEndX2.toCanvasX(scaleAndOffset)
    val rectEndY2 = windowData.rectEndY2.toCanvasY(scaleAndOffset)

    // Build the rectangle path
    with(rectanglePath) {
        reset()
        moveTo(rectStartX1, rectStartY1)
        lineTo(rectEndX1, rectEndY1)
        lineTo(rectEndX2, rectEndY2)
        lineTo(rectStartX2, rectStartY2)
        close()
    }

    // Draw the filled rectangle
    drawContext.canvas.drawPath(rectanglePath, fillPaint)

    // Draw the rectangle outline
    drawContext.canvas.drawPath(rectanglePath, linePaint)

    // Convert coordinates for the center line
    val centerStartX = windowData.centerStartX.toCanvasX(scaleAndOffset)
    val centerStartY = windowData.centerStartY.toCanvasY(scaleAndOffset)
    val centerEndX = windowData.centerEndX.toCanvasX(scaleAndOffset)
    val centerEndY = windowData.centerEndY.toCanvasY(scaleAndOffset)

    // Draw the center line
    drawContext.canvas.drawLine(Offset(centerStartX, centerStartY), Offset(centerEndX, centerEndY), linePaint)
}

fun DrawScope.renderOpening(openingData: WallOpeningData, scaleAndOffset: ScaleAndOffset) {
    // Paint for the opening fill
    val fillPaint = Paint().apply {
        color = Color.White
        style = PaintingStyle.Fill
        isAntiAlias = true
    }

    // Paint for the opening dashed border
    val strokePaint = Paint().apply {
        color = Color.Black
        style = PaintingStyle.Stroke
        strokeWidth = 1.dp.value
        isAntiAlias = true
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.value, 2.dp.value), 0f)
    }

    val rectanglePath = Path()

    // Convert coordinates for the outer rectangle of the opening
    val rectStartX1 = openingData.rectStartX1.toCanvasX(scaleAndOffset)
    val rectStartY1 = openingData.rectStartY1.toCanvasY(scaleAndOffset)
    val rectEndX1 = openingData.rectEndX1.toCanvasX(scaleAndOffset)
    val rectEndY1 = openingData.rectEndY1.toCanvasY(scaleAndOffset)
    val rectStartX2 = openingData.rectStartX2.toCanvasX(scaleAndOffset)
    val rectStartY2 = openingData.rectStartY2.toCanvasY(scaleAndOffset)
    val rectEndX2 = openingData.rectEndX2.toCanvasX(scaleAndOffset)
    val rectEndY2 = openingData.rectEndY2.toCanvasY(scaleAndOffset)

    // Build the rectangle path
    with(rectanglePath) {
        reset()
        moveTo(rectStartX1, rectStartY1)
        lineTo(rectEndX1, rectEndY1)
        lineTo(rectEndX2, rectEndY2)
        lineTo(rectStartX2, rectStartY2)
        close()
    }

    // Draw the filled rectangle
    drawContext.canvas.drawPath(rectanglePath, fillPaint)

    // Draw the dashed border
    drawContext.canvas.drawPath(rectanglePath, strokePaint)
}

fun DrawScope.renderDoor(
    doorData: WallDoorData,
    scaleAndOffset: ScaleAndOffset
) {
    // Paint for the door fill (Compose style)
    val doorFillColor = Color(0xFF8E95A3)
    val strokeWidth = 1.dp.value

    // Draw the door rectangle
    val rectStartX1 = doorData.rectStartX1.toCanvasX(scaleAndOffset)
    val rectStartY1 = doorData.rectStartY1.toCanvasY(scaleAndOffset)
    val rectEndX1 = doorData.rectEndX1.toCanvasX(scaleAndOffset)
    val rectEndY1 = doorData.rectEndY1.toCanvasY(scaleAndOffset)
    val rectStartX2 = doorData.rectStartX2.toCanvasX(scaleAndOffset)
    val rectStartY2 = doorData.rectStartY2.toCanvasY(scaleAndOffset)
    val rectEndX2 = doorData.rectEndX2.toCanvasX(scaleAndOffset)
    val rectEndY2 = doorData.rectEndY2.toCanvasY(scaleAndOffset)

    val path = Path().apply {
        moveTo(rectStartX1, rectStartY1)
        lineTo(rectEndX1, rectEndY1)
        lineTo(rectEndX2, rectEndY2)
        lineTo(rectStartX2, rectStartY2)
        close()
    }

    // Fill the door rectangle
    drawPath(
        path = path,
        color = doorFillColor,
        style = Fill
    )

    // Draw the border of the door
    drawPath(
        path = path,
        color = Color.Black,
        style = Stroke(width = strokeWidth)
    )

    // Draw the arc indicating door opening
    val arcStartX = doorData.arcStartX.toCanvasX(scaleAndOffset)
    val arcStartY = doorData.arcStartY.toCanvasY(scaleAndOffset)
    val arcRadius = doorData.arcRadius.toCanvasX(scaleAndOffset) - 0f.toCanvasX(scaleAndOffset)

    val arcRect = Rect(
        left = arcStartX - arcRadius,
        top = arcStartY - arcRadius,
        right = arcStartX + arcRadius,
        bottom = arcStartY + arcRadius
    )

    val doorArcPath = Path().apply {
        moveTo(rectStartX1, rectStartY1)
        arcTo(arcRect, doorData.angleDegrees + 90, -90f, false)
    }
    drawPath(
        path = doorArcPath,
        color = Color.Black,
        style = Stroke(width = strokeWidth)
    )
}

/*
fun DrawScope.renderDryingArea(
    dryingAreaData: DryingAreaData,
    scaleAndOffset: ScaleAndOffset
) {
    // Convert center coordinates to canvas coordinates
    val centerX = dryingAreaData.centerX.toCanvasX(scaleAndOffset)
    val centerY = dryingAreaData.centerY.toCanvasY(scaleAndOffset)

    // Create a text style
    val textStyle = TextStyle(
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    // Use a cross-platform FontFamily resolver
    val fontFamilyResolver = PlatformFontFamilyResolver()

    // Create the TextMeasurer
    val textMeasurer = TextMeasurer(
        defaultFontFamilyResolver = fontFamilyResolver,
        defaultDensity = density,
        defaultLayoutDirection = layoutDirection
    )

    // Measure the text layout
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(dryingAreaData.areaText),
        style = textStyle,
        constraints = Constraints(maxWidth = Int.MAX_VALUE)
    )

    // Measure and layout the text
    val textLayoutResult = textPainter.layout(
        text = dryingAreaData.areaText,
        style = textStyle,
        maxWidth = size.width
    )

    // Draw the text centered at the given position
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height
    val textOffset = Offset(
        x = centerX - textWidth / 2,
        y = centerY - textHeight / 2
    )

    drawText()
    textPainter.paint(
        canvas = drawContext.canvas,
        textLayoutResult = textLayoutResult,
        topLeft = textOffset
    )
}*/
