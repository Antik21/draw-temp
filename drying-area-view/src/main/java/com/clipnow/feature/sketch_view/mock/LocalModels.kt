package com.clipnow.feature.sketch_view.mock

import androidx.room.ColumnInfo

data class RoomEntity(
    val uid: String,
    val name: String,
    val dryingArea: DryingAreaEntity,
    val walls: List<WallEntity>
)

data class CornerEntity(
    @ColumnInfo("uid")
    val uid: String,
    @ColumnInfo("x")
    val x: Float,
    @ColumnInfo("y")
    val y: Float
)

data class DryingAreaEntity(
    @ColumnInfo("corners")
    val corners: List<CornerEntity>,
    @ColumnInfo("areaSquare")
    val areaSquare: Float,
)

data class WallEntity(
    @ColumnInfo("uid")
    val uid: String,
    @ColumnInfo("start")
    val start: CornerEntity,
    @ColumnInfo("end")
    val end: CornerEntity,
    @ColumnInfo("thickness")
    val thickness: Float,
    @ColumnInfo("windows")
    val windows: List<WallWindowEntity>?,
    @ColumnInfo("openings")
    val openings: List<WallOpeningEntity>?,
    @ColumnInfo("doors")
    val doors: List<WallDoorEntity>?,
)

data class WallDoorEntity(
    @ColumnInfo("uid")
    val uid: String,
    @ColumnInfo("startX")
    val startX: Float,
    @ColumnInfo("startY")
    val startY: Float,
    @ColumnInfo("endX")
    val endX: Float,
    @ColumnInfo("endY")
    val endY: Float,
)

data class WallOpeningEntity(
    @ColumnInfo("uid")
    val uid: String,
    @ColumnInfo("startX")
    val startX: Float,
    @ColumnInfo("startY")
    val startY: Float,
    @ColumnInfo("endX")
    val endX: Float,
    @ColumnInfo("endY")
    val endY: Float
)

data class WallWindowEntity(
    @ColumnInfo("uid")
    val uid: String,
    @ColumnInfo("startX")
    val startX: Float,
    @ColumnInfo("startY")
    val startY: Float,
    @ColumnInfo("endX")
    val endX: Float,
    @ColumnInfo("endY")
    val endY: Float
)