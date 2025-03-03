package com.clipnow.feature.sketch_view.renderer.data

import android.graphics.RectF


data class RoomData(val perimeter: PerimeterData, val dryingArea: DryingAreaData) {
    val bounds: RectF

    init {
        // Initialize min and max values for coordinates
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        // Find the minimum and maximum coordinates across all lines
        perimeter.walls.forEach { wall ->
            minX = minOf(minX, wall.startX, wall.endX)
            minY = minOf(minY, wall.startY, wall.endY)
            maxX = maxOf(maxX, wall.startX, wall.endX)
            maxY = maxOf(maxY, wall.startY, wall.endY)

            wall.lengthLines.forEach { line ->
                minX = minOf(minX, line.startX, line.endX)
                minY = minOf(minY, line.startY, line.endY)
                maxX = maxOf(maxX, line.startX, line.endX)
                maxY = maxOf(maxY, line.startY, line.endY)
            }
        }

        // Ensure that minX and minY start from zero
        // This means we translate all coordinates to ensure non-negative bounds
        val translatedMaxX = maxX - minX
        val translatedMaxY = maxY - minY

        bounds = RectF(
            0f,
            0f,
            translatedMaxX,
            translatedMaxY
        )
    }
}