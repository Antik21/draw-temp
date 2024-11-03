package com.clipnow.feature.drying_area_view.renderer


import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object RoomMathUtils {

    private const val FEET_PER_CM = 0.0328084f // 1 cm ≈ 0.0328084 feet
    private const val SQUARE_FEET_PER_SQUARE_CM = 0.00107639f

    // Calculates the angle in degrees between two points
    fun calculateAngle(startX: Float, startY: Float, endX: Float, endY: Float): Float {
        return Math.toDegrees(atan2((endY - startY).toDouble(), (endX - startX).toDouble())).toFloat()
    }

    // Calculates offset coordinates for a line based on the wall's angle and offset distance
    fun calculateOffsetCoordinates(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        offset: Float,
    ): Quadruple<Float, Float, Float, Float> {
        val dx = endX - startX
        val dy = endY - startY
        val length = sqrt(dx * dx + dy * dy)

        // Calculate offset values for x and y directions based on the wall's angle
        val offsetX = offset * dy / length
        val offsetY = offset * dx / length

        return when {
            dy == 0f -> {
                // Horizontal wall
                if (dx > 0) {
                    // Rightward wall (shift upwards by offset)
                    Quadruple(startX, startY - offset, endX, endY - offset)
                } else {
                    // Leftward wall (shift downwards by offset)
                    Quadruple(startX, startY + offset, endX, endY + offset)
                }
            }

            dx == 0f -> {
                // Vertical wall
                if (dy > 0) {
                    // Downward wall (shift rightwards by offset)
                    Quadruple(startX + offset, startY, endX + offset, endY)
                } else {
                    // Upward wall (shift leftwards by offset)
                    Quadruple(startX - offset, startY, endX - offset, endY)
                }
            }

            else -> {
                // Diagonal wall (not perfectly horizontal or vertical)
                Quadruple(
                    startX - offsetX,
                    startY + offsetY,
                    endX - offsetX,
                    endY + offsetY
                )
            }
        }
    }

    // Calculates the length between two points
    fun calculateLength(startX: Float, startY: Float, endX: Float, endY: Float): Float {
        val dx = endX - startX
        val dy = endY - startY
        return sqrt(dx * dx + dy * dy)
    }

    // Calculates the center coordinates for a set of walls given their start and end points
    fun calculateCenterCoordinates(wallCoordinates: List<Pair<Pair<Float, Float>, Pair<Float, Float>>>): Pair<Float, Float> {
        val xCoordinates = wallCoordinates.flatMap { listOf(it.first.first, it.second.first) }
        val yCoordinates = wallCoordinates.flatMap { listOf(it.first.second, it.second.second) }

        val centerX = (xCoordinates.minOrNull()!! + xCoordinates.maxOrNull()!!) / 2
        val centerY = (yCoordinates.minOrNull()!! + yCoordinates.maxOrNull()!!) / 2

        return Pair(centerX, centerY)
    }

    // Converts centimeters to feet
    fun convertCmToFeet(cm: Float): Float {
        return cm * FEET_PER_CM
    }

    // Converts square centimeters to square feet
    fun convertSquareCmToSquareFeet(squareCm: Float): Float {
        return squareCm * SQUARE_FEET_PER_SQUARE_CM
    }

    // Function to determine if the points are ordered clockwise or counterclockwise
    fun isClockwise(
        points: List<Pair<Float, Float>>,
    ): Boolean {
        var sum = 0f
        for (i in points.indices) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[(i + 1) % points.size]
            sum += (x2 - x1) * (y2 + y1)
        }
        return sum > 0
    }

    fun applyAdditionalOffset(x: Float, y: Float, angle: Float, offset: Float): Pair<Float, Float> {
        val offsetX = offset * cos(Math.toRadians(angle.toDouble())).toFloat()
        val offsetY = offset * sin(Math.toRadians(angle.toDouble())).toFloat()
        return x + offsetX to y + offsetY
    }

    fun isAtPoint(x1: Float, y1: Float, x2: Float, y2: Float, tolerance: Float = 0.1f): Boolean {
        return Math.abs(x1 - x2) <= tolerance && Math.abs(y1 - y2) <= tolerance
    }

    // Custom data class to hold four values as a return type for calculateOffsetCoordinates
    data class Quadruple<T, U, V, W>(val first: T, val second: U, val third: V, val fourth: W)

}