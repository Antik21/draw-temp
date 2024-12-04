package com.clipnow.feature.sketch_view.renderer.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.clipnow.feature.sketch_view.renderer.data.RoomData


@Composable
fun SketchComposeView(
    roomData: RoomData?,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (roomData == null) return@Canvas

        val bounds = roomData.bounds
        val padding = 32.dp.toPx()
        val scaleAndOffset = calculateScaleAndOffset(
            bounds,
            canvasWidth = size.width,
            canvasHeight = size.height,
            padding = padding
        )

        // Render perimeter
        renderPerimeter(roomData.perimeter, scaleAndOffset)

        // Render walls, openings, windows, doors, etc.
        roomData.perimeter.walls.forEach { wall ->
            wall.windows.forEach { renderWindow(it, scaleAndOffset) }
            wall.openings.forEach { renderOpening(it, scaleAndOffset) }
            wall.doors.forEach { renderDoor(it, scaleAndOffset) }
        }
    }
}