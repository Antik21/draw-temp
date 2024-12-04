package com.clipnow.feature.sketch_view.renderer.data


data class WallWindowData(
    val uid: String,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    // Rectangular coordinates
    val rectStartX1: Float,
    val rectStartY1: Float,
    val rectEndX1: Float,
    val rectEndY1: Float,
    val rectStartX2: Float,
    val rectStartY2: Float,
    val rectEndX2: Float,
    val rectEndY2: Float,
    // Center line
    val centerStartX: Float,
    val centerStartY: Float,
    val centerEndX: Float,
    val centerEndY: Float
)