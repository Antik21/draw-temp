package com.clipnow.feature.drying_area_view.renderer.data

data class WallData(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val lengthLines: List<LengthLineData>,
    val windows: List<WallWindowData>,
    val openings: List<WallOpeningData>,
    val doors: List<WallDoorData>,
)