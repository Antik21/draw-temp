package com.clipnow.feature.drying_area_view.mock

import com.google.gson.annotations.SerializedName


data class ApiCorner(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("origin")
    val origin: ApiPoint
)

data class ApiPoint(
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)

data class ApiWall(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("thickness")
    val thickness: Double,
    @SerializedName("windows")
    val windows: List<ApiWallWindow>?,
    @SerializedName("openings")
    val openings: List<ApiWallOpening>?,
    @SerializedName("doors")
    val doors: List<ApiWallDoor>?,
)

data class ApiWallWindow(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("start")
    val start: Double,
    @SerializedName("width")
    val width: Double,
)

data class ApiWallOpening(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("start")
    val start: Double,
    @SerializedName("width")
    val width: Double,
)

data class ApiWallDoor(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("start")
    val start: Double,
    @SerializedName("width")
    val width: Double,
    @SerializedName("isInside")
    val isInside: Boolean,
    @SerializedName("direction")
    val direction: Int,
    @SerializedName("target")
    val target: String,
)

// Модель для информации о комнате (data)
data class ApiRoomData(
    @SerializedName("name")
    val name: String,
    @SerializedName("ceilingHeight")
    val ceilingHeight: Double?,
    @SerializedName("corners")
    val corners: List<ApiCorner>,
    @SerializedName("walls")
    val walls: List<ApiWall>
)

// Модель для комнаты (room)
data class ApiRoom(
    @SerializedName("panoIds")
    val panoIds: List<String>,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: ApiRoomData,
    @SerializedName("totalDimensions")
    val totalDimensions: ApiTotalDimensions
)

// Модель для общих размеров комнаты (totalDimensions)
data class ApiTotalDimensions(
    @SerializedName("floorPerimeter")
    val floorPerimeter: Double,
    @SerializedName("ceilingPerimeter")
    val ceilingPerimeter: Double,
    @SerializedName("floorSquare")
    val floorSquare: Double,
    @SerializedName("ceilingSquare")
    val ceilingSquare: Double,
    @SerializedName("wallSquare")
    val wallSquare: Double,
    @SerializedName("volumeOfTheRoom")
    val volumeOfTheRoom: Double
)

// Модель для списка комнат
data class ApiRoomsResponse(
    @SerializedName("rooms")
    val rooms: List<ApiRoom>
)