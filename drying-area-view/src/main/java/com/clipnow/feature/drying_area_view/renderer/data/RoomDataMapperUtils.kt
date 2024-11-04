package com.clipnow.feature.drying_area_view.renderer.data

import com.clipnow.feature.drying_area_view.mock.CornerEntity
import com.clipnow.feature.drying_area_view.mock.WallDoorEntity
import com.clipnow.feature.drying_area_view.mock.WallEntity
import com.clipnow.feature.drying_area_view.mock.WallOpeningEntity
import com.clipnow.feature.drying_area_view.mock.WallWindowEntity
import kotlin.math.abs

// Функция для смещения угловых координат
fun applyOffsetToRoom(room: RoomData): RoomData {
    // Initialize min and max values for coordinates
    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE

    // Find the minimum and maximum coordinates across all length lines
    room.perimeter.walls.forEach { wall ->
        wall.lengthLines.forEach { line ->
            minX = minOf(minX, line.startX, line.endX)
            minY = minOf(minY, line.startY, line.endY)
        }
    }

    val offsetX = if (minX > 0) 0f else abs(minX)
    val offsetY = if (minY > 0) 0f else abs(minY)

    if (offsetX == 0f && offsetY == 0f) return room

    // Apply offset to LengthLineData
    val updatedWalls = room.perimeter.walls.map { wall ->
        val updatedLengthLines = wall.lengthLines.map { line ->
            LengthLineData(
                startX = line.startX + offsetX,
                startY = line.startY + offsetY,
                endX = line.endX + offsetX,
                endY = line.endY + offsetY,
                textX = line.textX + offsetX,
                textY = line.textY + offsetY,
                textAngle = line.textAngle,
                text = line.text,
                dashStartX = line.dashStartX + offsetX,
                dashStartY = line.dashStartY + offsetY,
                dashEndX = line.dashEndX + offsetX,
                dashEndY = line.dashEndY + offsetY
            )
        }

        // Apply offset to WallWindowData, WallOpeningData, and WallDoorData
        val updatedWindows = wall.windows.map { window ->
            WallWindowData(
                uid = window.uid,
                startX = window.startX + offsetX,
                startY = window.startY + offsetY,
                endX = window.endX + offsetX,
                endY = window.endY + offsetY
            )
        }

        val updatedOpenings = wall.openings.map { opening ->
            WallOpeningData(
                uid = opening.uid,
                startX = opening.startX + offsetX,
                startY = opening.startY + offsetY,
                endX = opening.endX + offsetX,
                endY = opening.endY + offsetY
            )
        }

        val updatedDoors = wall.doors.map { door ->
            WallDoorData(
                uid = door.uid,
                startX = door.startX + offsetX,
                startY = door.startY + offsetY,
                endX = door.endX + offsetX,
                endY = door.endY + offsetY,
                rectStartX1 = door.rectStartX1 + offsetX,
                rectStartY1 = door.rectStartY1 + offsetY,
                rectEndX1 = door.rectEndX1 + offsetX,
                rectEndY1 = door.rectEndY1 + offsetY,
                rectStartX2 = door.rectStartX2 + offsetX,
                rectStartY2 = door.rectStartY2 + offsetY,
                rectEndX2 = door.rectEndX2 + offsetX,
                rectEndY2 = door.rectEndY2 + offsetY,
                arcRadius = door.arcRadius,
                arcStartX = door.arcStartX + offsetX,
                arcStartY = door.arcStartY + offsetY,
                angleDegrees = door.angleDegrees
            )
        }

        WallData(
            startX = wall.startX + offsetX,
            startY = wall.startY + offsetY,
            endX = wall.endX + offsetX,
            endY = wall.endY + offsetY,
            lengthLines = updatedLengthLines,
            windows = updatedWindows,
            openings = updatedOpenings,
            doors = updatedDoors
        )
    }

    // Apply offset to DryingAreaData
    val updatedDryingArea = DryingAreaData(
        centerX = room.dryingArea.centerX + offsetX,
        centerY = room.dryingArea.centerY + offsetY,
        areaText = room.dryingArea.areaText
    )

    // Create updated RoomData with offset-applied walls and drying area
    val updatedPerimeter = PerimeterData(
        walls = updatedWalls,
        wallThickness = room.perimeter.wallThickness
    )

    return RoomData(
        perimeter = updatedPerimeter,
        dryingArea = updatedDryingArea
    )
}


// Функция для смещения угловых координат
fun applyOffsetToCorner(corner: CornerEntity, offset: Float): CornerEntity {
    return corner.copy(
        x = corner.x + offset,
        y = corner.y + offset
    )
}

// Функция для смещения данных стен, включая двери, окна и проёмы
fun applyOffsetToWall(wall: WallEntity, offset: Float): WallEntity {
    return wall.copy(
        start = applyOffsetToCorner(wall.start, offset),
        end = applyOffsetToCorner(wall.end, offset),
        windows = wall.windows?.map { applyOffsetToWindow(it, offset) },
        openings = wall.openings?.map { applyOffsetToOpening(it, offset) },
        doors = wall.doors?.map { applyOffsetToDoor(it, offset) }
    )
}

// Функция для смещения координат окна
fun applyOffsetToWindow(window: WallWindowEntity, offset: Float): WallWindowEntity {
    return window.copy(
        startX = window.startX + offset,
        startY = window.startY + offset,
        endX = window.endX + offset,
        endY = window.endY + offset
    )
}

// Функция для смещения координат проёма
fun applyOffsetToOpening(opening: WallOpeningEntity, offset: Float): WallOpeningEntity {
    return opening.copy(
        startX = opening.startX + offset,
        startY = opening.startY + offset,
        endX = opening.endX + offset,
        endY = opening.endY + offset
    )
}

// Функция для смещения координат двери
fun applyOffsetToDoor(door: WallDoorEntity, offset: Float): WallDoorEntity {
    return door.copy(
        startX = door.startX + offset,
        startY = door.startY + offset,
        endX = door.endX + offset,
        endY = door.endY + offset
    )
}