package com.clipnow.feature.sketch_view.renderer.data


import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object RoomMathUtils {

    private const val FEET_PER_CM = 0.0328084f // 1 cm ≈ 0.0328084 feet
    private const val SQUARE_FEET_PER_SQUARE_CM = 0.00107639f
    private const val CM_PER_INCH = 2.54f // 1 inch = 2.54 cm
    private const val INCH_PER_FOOT = 12f

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
        offset: Float
    ): Quadruple<Float, Float, Float, Float> {
        // Calculate the angle of the line segment
        val angle = atan2((endY - startY).toDouble(), (endX - startX).toDouble()).toFloat()

        // Calculate offset adjustments
        val offsetX = offset * sin(angle)
        val offsetY = offset * cos(angle)

        // Apply offset to start and end coordinates
        val offsetStartX = startX + offsetX
        val offsetStartY = startY - offsetY
        val offsetEndX = endX + offsetX
        val offsetEndY = endY - offsetY

        return Quadruple(offsetStartX, offsetStartY, offsetEndX, offsetEndY)
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
            .distinct()
            .sorted()
        val yCoordinates = wallCoordinates.flatMap { listOf(it.first.second, it.second.second) }
            .distinct()
            .sorted()

        // Initialize variables for the center coordinates
        var centerX = xCoordinates.first()
        var centerY = yCoordinates.first()

        // Iterate over x and y coordinates to find the valid center
        for (i in xCoordinates.indices) {
            for (j in yCoordinates.indices) {
                centerX = (xCoordinates[0] + xCoordinates[xCoordinates.size - 1 - i]) / 2
                centerY = (yCoordinates[0] + yCoordinates[yCoordinates.size - 1 - j]) / 2

                // Check if the calculated point is inside the perimeter
                if (isInsidePerimeter(wallCoordinates, centerX, centerY)) {
                    return centerX to centerY
                }
            }
        }

        return centerX to centerY
    }

    // Checks if a point lies inside the perimeter defined by wall coordinates
    private fun isInsidePerimeter(
        coordinates: List<Pair<Pair<Float, Float>, Pair<Float, Float>>>,
        pointX: Float,
        pointY: Float
    ): Boolean {
        var intersections = 0

        // Iterate through each wall segment
        for ((start, end) in coordinates) {
            val (startX, startY) = start
            val (endX, endY) = end

            // Check if the point lies on the same Y level as the wall segment
            if (pointY > minOf(startY, endY) && pointY <= maxOf(startY, endY) && pointX <= maxOf(startX, endX)) {
                // Calculate the X coordinate of the intersection point
                val intersectionX =
                    if (startY != endY) startX + (pointY - startY) * (endX - startX) / (endY - startY)
                    else startX

                // Count intersections only if the intersection is to the right of the point
                if (intersectionX > pointX) {
                    intersections++
                }
            }
        }

        // A point is inside if the number of intersections is odd
        return intersections % 2 == 1
    }

    // Converts centimeters to feet
    fun convertCmToFeet(cm: Float): Float {
        return cm * FEET_PER_CM
    }

    // Converts centimeters to inches
    fun cmToInches(cm: Float): Float {
        return cm / CM_PER_INCH
    }

    // Converts inches to feet and inches (e.g., 25.5 inches = 2 feet 1.5 inches)
    fun inchesToFeetAndInches(inches: Float): Pair<Int, Float> {
        val feet = (inches / INCH_PER_FOOT).toInt()
        val remainingInches = inches % INCH_PER_FOOT
        return Pair(feet, remainingInches)
    }

    // Combined function to convert centimeters to feet and inches
    fun cmToFeetAndInches(cm: Float): Pair<Int, Float> {
        val inches = cmToInches(cm)
        return inchesToFeetAndInches(inches)
    }

    // Converts square centimeters to square feet
    fun convertSquareCmToSquareFeet(squareCm: Float): Float {
        return squareCm * SQUARE_FEET_PER_SQUARE_CM
    }

    fun isAtPoint(x1: Float, y1: Float, x2: Float, y2: Float, tolerance: Float = 0.1f): Boolean {
        return abs(x1 - x2) <= tolerance && abs(y1 - y2) <= tolerance
    }

    // Custom data class to hold four values as a return type for calculateOffsetCoordinates
    data class Quadruple<T, U, V, W>(val first: T, val second: U, val third: V, val fourth: W)

}
