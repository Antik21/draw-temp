package com.clipnow.feature.drying_area_view.mock

import kotlin.math.sqrt


class RoomMapper {

    // Mapping corners (ApiCorner) to coordinate pairs (x, y)
    private fun mapCorners(apiCorners: List<ApiCorner>): List<CornerEntity> {
        return apiCorners.map {
            CornerEntity(it.uid, it.origin.x.toFloat(), it.origin.y.toFloat())
        }
    }

    // Calculate wall length once and reuse it
    private fun calculateWallLength(start: CornerEntity, end: CornerEntity): Float {
        val dx = end.x - start.x
        val dy = end.y - start.y
        return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    // Map windows (ApiWallWindow) with pre-calculated wallLength
    private fun mapWindows(
        apiWindows: List<ApiWallWindow>,
        wallStart: CornerEntity,
        wallEnd: CornerEntity,
        wallLength: Float
    ): List<WallWindowEntity> {
        return apiWindows.map { window ->
            val (startX, startY, endX, endY) = calculatePositions(
                wallStart,
                wallEnd,
                wallLength,
                window.start,
                window.width
            )
            WallWindowEntity(window.uid, startX, startY, endX, endY)
        }
    }

    private fun mapOpenings(
        apiOpenings: List<ApiWallOpening>,
        wallStart: CornerEntity,
        wallEnd: CornerEntity,
        wallLength: Float
    ): List<WallOpeningEntity> {
        return apiOpenings.map { opening ->
            val (startX, startY, endX, endY) = calculatePositions(
                wallStart,
                wallEnd,
                wallLength,
                opening.start,
                opening.width
            )
            WallOpeningEntity(opening.uid, startX, startY, endX, endY)
        }
    }

    private fun mapDoors(
        apiDoors: List<ApiWallDoor>,
        wallStart: CornerEntity,
        wallEnd: CornerEntity,
        wallLength: Float
    ): List<WallDoorEntity> {
        return apiDoors.map { door ->
            val (startX, startY, endX, endY) = calculatePositions(
                wallStart,
                wallEnd,
                wallLength,
                door.start,
                door.width
            )
            WallDoorEntity(door.uid, startX, startY, endX, endY)
        }
    }

    // Calculate positions along the wall based on start and width
    private fun calculatePositions(
        wallStart: CornerEntity,
        wallEnd: CornerEntity,
        wallLength: Float,
        startOffsetRatio: Double,
        width: Double
    ): List<Float> {
        val startOffset = wallLength * startOffsetRatio.toFloat()
        val endOffset = startOffset + width.toFloat()

        val startX = interpolateCoordinate(wallStart.x, wallEnd.x, startOffset / wallLength)
        val startY = interpolateCoordinate(wallStart.y, wallEnd.y, startOffset / wallLength)
        val endX = interpolateCoordinate(wallStart.x, wallEnd.x, endOffset / wallLength)
        val endY = interpolateCoordinate(wallStart.y, wallEnd.y, endOffset / wallLength)

        return listOf(startX, startY, endX, endY)
    }

    private fun interpolateCoordinate(start: Float, end: Float, factor: Float): Float {
        return start + (end - start) * factor
    }

    private fun mapWalls(apiWalls: List<ApiWall>, corners: List<CornerEntity>): List<WallEntity> {
        return apiWalls.map { apiWall ->
            val startCorner = corners.find { it.uid == apiWall.start }
                ?: throw IllegalArgumentException("Corner with id ${apiWall.start} not found")
            val endCorner = corners.find { it.uid == apiWall.end }
                ?: throw IllegalArgumentException("Corner with id ${apiWall.end} not found")

            val wallLength = calculateWallLength(startCorner, endCorner)

            WallEntity(
                uid = apiWall.uid,
                start = startCorner,
                end = endCorner,
                thickness = apiWall.thickness.toFloat(),
                windows = apiWall.windows?.let { mapWindows(it, startCorner, endCorner, wallLength) } ?: emptyList(),
                openings = apiWall.openings?.let { mapOpenings(it, startCorner, endCorner, wallLength) }
                    ?: emptyList(),
                doors = apiWall.doors?.let { mapDoors(it, startCorner, endCorner, wallLength) }
                    ?: emptyList(),
            )
        }
    }

    private fun mapDryingArea(corners: List<CornerEntity>, floorArea: Double): DryingAreaEntity {
        return DryingAreaEntity(corners, areaSquare = floorArea.toFloat())
    }

    fun mapRoom(apiRoom: ApiRoom): RoomEntity {
        val corners = mapCorners(apiRoom.data.corners)
        val walls = mapWalls(apiRoom.data.walls, corners)
        val dryingArea = mapDryingArea(corners, apiRoom.totalDimensions.floorSquare)

        return RoomEntity(
            uid = apiRoom.panoIds.first(),
            name = apiRoom.data.name,
            walls = walls,
            dryingArea = dryingArea,
        )
    }

    fun mapRooms(apiRooms: List<ApiRoom>): List<RoomEntity> {
        return apiRooms.map { mapRoom(it) }
    }
}