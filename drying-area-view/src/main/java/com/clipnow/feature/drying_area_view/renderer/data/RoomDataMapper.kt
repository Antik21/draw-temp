package com.clipnow.feature.drying_area_view.renderer.data

import android.content.Context
import android.graphics.RectF
import com.clipnow.feature.drying_area_view.mock.RoomEntity
import com.clipnow.feature.drying_area_view.mock.WallDoorEntity
import com.clipnow.feature.drying_area_view.mock.WallEntity
import com.clipnow.feature.drying_area_view.mock.WallOpeningEntity
import com.clipnow.feature.drying_area_view.mock.WallWindowEntity
import com.clipnow.feature.drying_area_view.renderer.RoomMathUtils
import com.clipnow.feature.drying_area_view.renderer.dpToPx
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class RoomDataMapper(private val context: Context) {

    fun mapRoom(roomEntity: RoomEntity): RoomData {
        val wallThickness = roomEntity.walls.firstOrNull()?.thickness ?: 0f

        val walls = mapWalls(roomEntity.walls, wallThickness)
        val wallCoordinates = walls.map { Pair(Pair(it.startX, it.startY), Pair(it.endX, it.endY)) }
        val (centerX, centerY) = RoomMathUtils.calculateCenterCoordinates(wallCoordinates)

        val dryingArea = DryingAreaData(
            centerX = centerX,
            centerY = centerY,
            areaText = String.format("%.2f ft²", RoomMathUtils.convertSquareCmToSquareFeet(roomEntity.dryingArea.areaSquare))
        )

        val perimeter = PerimeterData(
            walls = walls,
            wallThickness = wallThickness
        )

        return RoomData(
            perimeter = perimeter,
            dryingArea = dryingArea
        )
    }

    private fun mapWalls(wallEntities: List<WallEntity>, wallThickness: Float): List<WallData> {
        return wallEntities.map { wallEntity ->
            val lengthLines = mapLengthLines(wallEntity)

            WallData(
                startX = wallEntity.start.x,
                startY = wallEntity.start.y,
                endX = wallEntity.end.x,
                endY = wallEntity.end.y,
                lengthLines = lengthLines,
                windows = mapWallWindows(wallEntity.windows),
                openings = mapWallOpenings(wallEntity.openings),
                doors = mapWallDoors(wallEntity.doors, wallThickness)
            )
        }
    }

    private fun mapLengthLines(wallEntity: WallEntity): List<LengthLineData> {
        val lengthLines = mutableListOf<LengthLineData>()
        val wallThickness = wallEntity.thickness

        // Calculate the total wall length
        val wallLength = RoomMathUtils.calculateLength(wallEntity.start.x, wallEntity.start.y, wallEntity.end.x, wallEntity.end.y)
        val wallLengthFt = RoomMathUtils.convertCmToFeet(wallLength)

        // Offset for the entire wall length line (considering the wall thickness)
        val wallOffset = context.dpToPx(RoomDataConsts.WALL_LENGTH_LINE_OFFSET + wallThickness / 2)
        val additionalOffset = context.dpToPx(wallThickness / 2)  // Additional offset to account for wall thickness

        // Calculate angle for wall
        val angle = RoomMathUtils.calculateAngle(wallEntity.start.x, wallEntity.start.y, wallEntity.end.x, wallEntity.end.y)

        // Calculate coordinates for the offset line representing the entire wall length
        val (startXOffset, startYOffset, endXOffset, endYOffset) = RoomMathUtils.calculateOffsetCoordinates(
            wallEntity.start.x, wallEntity.start.y, wallEntity.end.x, wallEntity.end.y, wallOffset
        )

        // Adjust the start and end coordinates by additional offset to extend the line beyond the wall
        val adjustedStartX = startXOffset - additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
        val adjustedStartY = startYOffset - additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
        val adjustedEndX = endXOffset + additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
        val adjustedEndY = endYOffset + additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()

        // Center of the text for the entire wall length
        val textX = (adjustedStartX + adjustedEndX) / 2
        val textY = (adjustedStartY + adjustedEndY) / 2

        // Add the entire wall length line with adjusted start and end points
        lengthLines.add(
            LengthLineData(
                startX = adjustedStartX,
                startY = adjustedStartY,
                endX = adjustedEndX,
                endY = adjustedEndY,
                text = String.format("%.1f ft", wallLengthFt),
                textX = textX,
                textY = textY,
                textAngle = angle
            )
        )

        // Extract segments from windows, openings, and doors
        val windowSegments = wallEntity.windows?.map { it.startX to it.endX } ?: emptyList()
        val openingSegments = wallEntity.openings?.map { it.startX to it.endX } ?: emptyList()
        val doorSegments = wallEntity.doors?.map { it.startX to it.endX } ?: emptyList()

        // Combine all segments and sort by the start of each segment
        val segments = (windowSegments + openingSegments + doorSegments).sortedBy { it.first }

        // Set the offset for segment length lines
        val segmentOffset = context.dpToPx(RoomDataConsts.WALL_SEGMENT_LENGTH_LINE_OFFSET + wallThickness / 2)
        var currentStart = wallEntity.start.x

        for ((startX, endX) in segments) {
            if (currentStart < startX) {
                // Calculate the segment between currentStart and startX
                val segmentLength = RoomMathUtils.calculateLength(currentStart, wallEntity.start.y, startX, wallEntity.start.y)
                val segmentLengthFt = RoomMathUtils.convertCmToFeet(segmentLength)

                // Calculate offset coordinates for segment
                val (segmentStartXOffset, segmentStartYOffset, segmentEndXOffset, segmentEndYOffset) = RoomMathUtils.calculateOffsetCoordinates(
                    currentStart, wallEntity.start.y, startX, wallEntity.start.y, segmentOffset
                )

                // Adjust segment coordinates for start and end points if they match wall's start or end
                val adjustedSegmentStartX = if (currentStart == wallEntity.start.x) {
                    segmentStartXOffset - additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentStartXOffset
                val adjustedSegmentStartY = if (currentStart == wallEntity.start.x) {
                    segmentStartYOffset - additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentStartYOffset
                val adjustedSegmentEndX = if (startX == wallEntity.end.x) {
                    segmentEndXOffset + additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentEndXOffset
                val adjustedSegmentEndY = if (startX == wallEntity.end.x) {
                    segmentEndYOffset + additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentEndYOffset

                // Calculate center of the text for the segment length
                val segmentTextX = (adjustedSegmentStartX + adjustedSegmentEndX) / 2
                val segmentTextY = (adjustedSegmentStartY + adjustedSegmentEndY) / 2

                lengthLines.add(
                    LengthLineData(
                        startX = adjustedSegmentStartX,
                        startY = adjustedSegmentStartY,
                        endX = adjustedSegmentEndX,
                        endY = adjustedSegmentEndY,
                        text = String.format("%.1f ft", segmentLengthFt),
                        textX = segmentTextX,
                        textY = segmentTextY,
                        textAngle = angle
                    )
                )
            }
            // Move currentStart to the end of the current segment
            currentStart = endX
        }

        // Add the final segment if there's wall remaining
        if (currentStart < wallEntity.end.x) {
            val segmentLength = RoomMathUtils.calculateLength(currentStart, wallEntity.start.y, wallEntity.end.x, wallEntity.start.y)
            val segmentLengthFt = RoomMathUtils.convertCmToFeet(segmentLength)

            // Calculate offset coordinates for the last segment
            val (segmentStartXOffset, segmentStartYOffset, segmentEndXOffset, segmentEndYOffset) = RoomMathUtils.calculateOffsetCoordinates(
                currentStart, wallEntity.start.y, wallEntity.end.x, wallEntity.start.y, segmentOffset
            )

            // Adjust final segment's coordinates if it matches wall's end
            val adjustedSegmentStartX = if (currentStart == wallEntity.start.x) {
                segmentStartXOffset - additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
            } else segmentStartXOffset
            val adjustedSegmentStartY = if (currentStart == wallEntity.start.x) {
                segmentStartYOffset - additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
            } else segmentStartYOffset
            val adjustedSegmentEndX = if (wallEntity.end.x == wallEntity.end.x) {
                segmentEndXOffset + additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
            } else segmentEndXOffset
            val adjustedSegmentEndY = if (wallEntity.end.x == wallEntity.end.x) {
                segmentEndYOffset + additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
            } else segmentEndYOffset

            // Center of the text for the last segment
            val segmentTextX = (adjustedSegmentStartX + adjustedSegmentEndX) / 2
            val segmentTextY = (adjustedSegmentStartY + adjustedSegmentEndY) / 2

            lengthLines.add(
                LengthLineData(
                    startX = adjustedSegmentStartX,
                    startY = adjustedSegmentStartY,
                    endX = adjustedSegmentEndX,
                    endY = adjustedSegmentEndY,
                    text = String.format("%.1f ft", segmentLengthFt),
                    textX = segmentTextX,
                    textY = segmentTextY,
                    textAngle = angle
                )
            )
        }

        return lengthLines
    }

    private fun mapWallWindows(windows: List<WallWindowEntity>?): List<WallWindowData> {
        return windows?.map { window ->
            WallWindowData(
                uid = window.uid,
                startX = window.startX,
                startY = window.startY,
                endX = window.endX,
                endY = window.endY
            )
        } ?: emptyList()
    }

    private fun mapWallOpenings(openings: List<WallOpeningEntity>?): List<WallOpeningData> {
        return openings?.map { opening ->
            WallOpeningData(
                uid = opening.uid,
                startX = opening.startX,
                startY = opening.startY,
                endX = opening.endX,
                endY = opening.endY
            )
        } ?: emptyList()
    }

    private fun mapWallDoors(doors: List<WallDoorEntity>?, wallThickness: Float): List<WallDoorData> {
        val doorThickness = context.dpToPx(wallThickness + 1)
        return doors?.map { door ->
            // Вычисляем угол двери
            val doorLength = sqrt((door.endX - door.startX).pow(2) + (door.endY - door.startY).pow(2))
            val angle = atan2((door.endY - door.startY).toDouble(), (door.endX - door.startX).toDouble()).toFloat()
            val arcRadius = doorLength
            val arcStartX = door.endX + cos(angle + Math.PI.toFloat() / 2) * arcRadius
            val arcStartY = door.endY + sin(angle + Math.PI.toFloat() / 2) * arcRadius

            // Вычисляем смещение по нормали для толщины двери
            val offsetX = (doorThickness / 2) * sin(angle)
            val offsetY = (doorThickness / 2) * cos(angle)

            // Вычисляем координаты четырех углов прямоугольника двери
            val rectStartX1 = door.startX + offsetX
            val rectStartY1 = door.startY - offsetY
            val rectEndX1 = door.endX + offsetX
            val rectEndY1 = door.endY - offsetY

            val rectStartX2 = door.startX - offsetX
            val rectStartY2 = door.startY + offsetY
            val rectEndX2 = door.endX - offsetX
            val rectEndY2 = door.endY + offsetY

            WallDoorData(
                uid = door.uid,
                startX = door.startX,
                startY = door.startY,
                endX = door.endX,
                endY = door.endY,
                rectStartX1 = rectStartX1,
                rectStartY1 = rectStartY1,
                rectEndX1 = rectEndX1,
                rectEndY1 = rectEndY1,
                rectStartX2 = rectStartX2,
                rectStartY2 = rectStartY2,
                rectEndX2 = rectEndX2,
                rectEndY2 = rectEndY2,
                arcRadius = arcRadius,
                arcStartX = arcStartX,
                arcStartY = arcStartY,
                angleDegrees = Math.toDegrees(angle.toDouble()).toFloat()
            )
        } ?: emptyList()
    }
}
