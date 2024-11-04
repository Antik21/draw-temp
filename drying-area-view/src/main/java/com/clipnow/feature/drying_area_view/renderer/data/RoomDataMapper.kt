package com.clipnow.feature.drying_area_view.renderer.data

import android.content.Context
import com.clipnow.feature.drying_area_view.mock.RoomEntity
import com.clipnow.feature.drying_area_view.mock.WallDoorEntity
import com.clipnow.feature.drying_area_view.mock.WallEntity
import com.clipnow.feature.drying_area_view.mock.WallOpeningEntity
import com.clipnow.feature.drying_area_view.mock.WallWindowEntity
import com.clipnow.feature.drying_area_view.renderer.RoomMathUtils
import com.clipnow.feature.drying_area_view.renderer.dpToPx
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class RoomDataMapper(private val context: Context) {

    fun mapRoom(roomEntity: RoomEntity): RoomData {
        val wallThickness = roomEntity.walls.firstOrNull()?.thickness ?: 0f

        val walls = mapWalls(roomEntity.walls, wallThickness)
        val wallCoordinates = walls.map { Pair(Pair(it.startX, it.startY), Pair(it.endX, it.endY)) }
        val (centerX, centerY) = RoomMathUtils.calculateCenterCoordinates(wallCoordinates)

        val dryingArea = DryingAreaData(
            centerX = centerX,
            centerY = centerY,
            areaText = String.format(
                "%.2f ft²",
                RoomMathUtils.convertSquareCmToSquareFeet(roomEntity.dryingArea.areaSquare)
            )
        )

        val perimeter = PerimeterData(
            walls = walls,
            wallThickness = wallThickness
        )

        val roomData = RoomData(
            perimeter = perimeter,
            dryingArea = dryingArea
        )

        return applyOffsetToRoom(roomData)
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
        val wallLength =
            RoomMathUtils.calculateLength(wallEntity.start.x, wallEntity.start.y, wallEntity.end.x, wallEntity.end.y)
        val wallLengthFt = RoomMathUtils.convertCmToFeet(wallLength)

        // Offset for the entire wall length line (considering the wall thickness)
        val wallOffset = context.dpToPx(RoomDataConsts.WALL_LENGTH_LINE_OFFSET + wallThickness / 2)
        val additionalOffset = context.dpToPx(wallThickness / 2)  // Additional offset to account for wall thickness

        // Calculate angle for wall
        val angle =
            RoomMathUtils.calculateAngle(wallEntity.start.x, wallEntity.start.y, wallEntity.end.x, wallEntity.end.y)

        // Calculate coordinates for the offset line representing the entire wall length
        val (startXOffset, startYOffset, endXOffset, endYOffset) = RoomMathUtils.calculateOffsetCoordinates(
            wallEntity.start.x, wallEntity.start.y, wallEntity.end.x, wallEntity.end.y, wallOffset
        )

        // Adjust the start and end coordinates by additional offset to extend the line beyond the wall
        val adjustedStartX = startXOffset - additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
        val adjustedStartY = startYOffset - additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
        val adjustedEndX = endXOffset + additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
        val adjustedEndY = endYOffset + additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()

        // Calculate dash line start and end points for the wall length line
        val dashStartX = adjustedStartX - wallOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
        val dashStartY = adjustedStartY + wallOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
        val dashEndX = adjustedEndX - wallOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
        val dashEndY = adjustedEndY + wallOffset * cos(Math.toRadians(angle.toDouble())).toFloat()

        // Center of the text for the entire wall length
        val textX = (adjustedStartX + adjustedEndX) / 2
        val textY = (adjustedStartY + adjustedEndY) / 2

        // Add the entire wall length line with adjusted start and end points, and updated dash points
        lengthLines.add(
            LengthLineData(
                startX = adjustedStartX,
                startY = adjustedStartY,
                endX = adjustedEndX,
                endY = adjustedEndY,
                text = String.format(Locale.US, "%.1f ft", wallLengthFt),
                textX = textX,
                textY = textY,
                textAngle = angle,
                dashStartX = dashStartX,
                dashStartY = dashStartY,
                dashEndX = dashEndX,
                dashEndY = dashEndY
            )
        )

        // Processing segments for windows, openings, and doors
        val windowSegments = wallEntity.windows?.map { Segment(it.startX, it.startY, it.endX, it.endY) } ?: emptyList()
        val openingSegments =
            wallEntity.openings?.map { Segment(it.startX, it.startY, it.endX, it.endY) } ?: emptyList()
        val doorSegments = wallEntity.doors?.map { Segment(it.startX, it.startY, it.endX, it.endY) } ?: emptyList()

        // Combine all segments and sort by their distance along the wall
        val segments = (windowSegments + openingSegments + doorSegments).sortedBy { segment ->
            RoomMathUtils.calculateLength(wallEntity.start.x, wallEntity.start.y, segment.startX, segment.startY)
        }

        // Set the offset for segment length lines
        val segmentOffset = context.dpToPx(RoomDataConsts.WALL_SEGMENT_LENGTH_LINE_OFFSET + wallThickness / 2)
        var currentStartX = wallEntity.start.x
        var currentStartY = wallEntity.start.y

        for (segment in segments) {
            if (!RoomMathUtils.isAtPoint(currentStartX, currentStartY, segment.startX, segment.startY)) {
                // Calculate the segment between current start and segment start
                val segmentLength =
                    RoomMathUtils.calculateLength(currentStartX, currentStartY, segment.startX, segment.startY)
                val segmentLengthFt = RoomMathUtils.convertCmToFeet(segmentLength)

                // Calculate offset coordinates for segment
                val (segmentStartXOffset, segmentStartYOffset, segmentEndXOffset, segmentEndYOffset) = RoomMathUtils.calculateOffsetCoordinates(
                    currentStartX, currentStartY, segment.startX, segment.startY, segmentOffset
                )

                // Adjust segment coordinates if it matches wall's start or end
                val adjustedSegmentStartX =
                    if (RoomMathUtils.isAtPoint(currentStartX, currentStartY, wallEntity.start.x, wallEntity.start.y)) {
                        segmentStartXOffset - additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
                    } else segmentStartXOffset
                val adjustedSegmentStartY =
                    if (RoomMathUtils.isAtPoint(currentStartX, currentStartY, wallEntity.start.x, wallEntity.start.y)) {
                        segmentStartYOffset - additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                    } else segmentStartYOffset
                val adjustedSegmentEndX =
                    if (RoomMathUtils.isAtPoint(segment.startX, segment.startY, wallEntity.end.x, wallEntity.end.y)) {
                        segmentEndXOffset + additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
                    } else segmentEndXOffset
                val adjustedSegmentEndY =
                    if (RoomMathUtils.isAtPoint(segment.startX, segment.startY, wallEntity.end.x, wallEntity.end.y)) {
                        segmentEndYOffset + additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                    } else segmentEndYOffset

                // Calculate dash points for the segment length
                val dashSegmentStartX =
                    adjustedSegmentStartX - segmentOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                val dashSegmentStartY =
                    adjustedSegmentStartY + segmentOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
                val dashSegmentEndX =
                    adjustedSegmentEndX - segmentOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                val dashSegmentEndY =
                    adjustedSegmentEndY + segmentOffset * cos(Math.toRadians(angle.toDouble())).toFloat()

                // Calculate center of the text for the segment length
                val segmentTextX = (adjustedSegmentStartX + adjustedSegmentEndX) / 2
                val segmentTextY = (adjustedSegmentStartY + adjustedSegmentEndY) / 2

                lengthLines.add(
                    LengthLineData(
                        startX = adjustedSegmentStartX,
                        startY = adjustedSegmentStartY,
                        endX = adjustedSegmentEndX,
                        endY = adjustedSegmentEndY,
                        text = String.format(Locale.US,"%.1f ft", segmentLengthFt),
                        textX = segmentTextX,
                        textY = segmentTextY,
                        textAngle = angle,
                        dashStartX = dashSegmentStartX,
                        dashStartY = dashSegmentStartY,
                        dashEndX = dashSegmentEndX,
                        dashEndY = dashSegmentEndY
                    )
                )
            }
            // Move current start to the end of the current segment
            currentStartX = segment.endX
            currentStartY = segment.endY
        }

        // Add the final segment if there's wall remaining
        if (!RoomMathUtils.isAtPoint(currentStartX, currentStartY, wallEntity.end.x, wallEntity.end.y)) {
            val segmentLength =
                RoomMathUtils.calculateLength(currentStartX, currentStartY, wallEntity.end.x, wallEntity.end.y)
            val segmentLengthFt = RoomMathUtils.convertCmToFeet(segmentLength)

            // Calculate offset coordinates for the last segment
            val (segmentStartXOffset, segmentStartYOffset, segmentEndXOffset, segmentEndYOffset) = RoomMathUtils.calculateOffsetCoordinates(
                currentStartX, currentStartY, wallEntity.end.x, wallEntity.end.y, segmentOffset
            )

            // Adjust final segment's coordinates if it matches wall's end
            val adjustedSegmentStartX =
                if (RoomMathUtils.isAtPoint(currentStartX, currentStartY, wallEntity.start.x, wallEntity.start.y)) {
                    segmentStartXOffset - additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentStartXOffset
            val adjustedSegmentStartY =
                if (RoomMathUtils.isAtPoint(currentStartX, currentStartY, wallEntity.start.x, wallEntity.start.y)) {
                    segmentStartYOffset - additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentStartYOffset
            val adjustedSegmentEndX =
                if (RoomMathUtils.isAtPoint(wallEntity.end.x, wallEntity.end.y, wallEntity.end.x, wallEntity.end.y)) {
                    segmentEndXOffset + additionalOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentEndXOffset
            val adjustedSegmentEndY =
                if (RoomMathUtils.isAtPoint(wallEntity.end.x, wallEntity.end.y, wallEntity.end.x, wallEntity.end.y)) {
                    segmentEndYOffset + additionalOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
                } else segmentEndYOffset

            // Calculate dash points for the final segment length
            val dashSegmentStartX =
                adjustedSegmentStartX - segmentOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
            val dashSegmentStartY =
                adjustedSegmentStartY + segmentOffset * cos(Math.toRadians(angle.toDouble())).toFloat()
            val dashSegmentEndX = adjustedSegmentEndX - segmentOffset * sin(Math.toRadians(angle.toDouble())).toFloat()
            val dashSegmentEndY = adjustedSegmentEndY + segmentOffset * cos(Math.toRadians(angle.toDouble())).toFloat()

            // Center of the text for the last segment
            val segmentTextX = (adjustedSegmentStartX + adjustedSegmentEndX) / 2
            val segmentTextY = (adjustedSegmentStartY + adjustedSegmentEndY) / 2

            lengthLines.add(
                LengthLineData(
                    startX = adjustedSegmentStartX,
                    startY = adjustedSegmentStartY,
                    endX = adjustedSegmentEndX,
                    endY = adjustedSegmentEndY,
                    text = String.format(Locale.US,"%.1f ft", segmentLengthFt),
                    textX = segmentTextX,
                    textY = segmentTextY,
                    textAngle = angle,
                    dashStartX = dashSegmentStartX,
                    dashStartY = dashSegmentStartY,
                    dashEndX = dashSegmentEndX,
                    dashEndY = dashSegmentEndY
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
            val doorLength = RoomMathUtils.calculateLength(door.startX, door.startY, door.endX, door.endY)
            val angle = atan2((door.endY - door.startY).toDouble(), (door.endX - door.startX).toDouble()).toFloat()

            // Смещение по нормали для толщины двери
            val offsetX = (doorThickness / 2) * sin(angle)
            val offsetY = (doorThickness / 2) * cos(angle)

            // Координаты четырёх углов прямоугольника двери
            val rectStartX1 = door.startX + offsetX
            val rectStartY1 = door.startY - offsetY
            val rectEndX1 = door.endX + offsetX
            val rectEndY1 = door.endY - offsetY
            val rectStartX2 = door.startX - offsetX
            val rectStartY2 = door.startY + offsetY
            val rectEndX2 = door.endX - offsetX
            val rectEndY2 = door.endY + offsetY


            // Начальная точка дуги, отступающая от начала двери на arcOffset единиц
            val arcStartX = door.startX - cos(angle)
            val arcStartY = door.startY - sin(angle)

            // Радиус дуги
            val arcRadius = doorLength

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


    private data class Segment(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float
    )
}
