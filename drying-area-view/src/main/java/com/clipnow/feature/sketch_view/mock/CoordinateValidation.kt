package com.clipnow.feature.sketch_view.mock


// Validates and adjusts wall coordinates to ensure clockwise order
fun validateAndFixWallCoordinates(walls: List<ApiWall>, corners: List<ApiCorner>): List<ApiWall> {
    val orderedWalls = mutableListOf<ApiWall>() // Список стен в правильной последовательности
    val remainingWalls = walls.toMutableList() // Список оставшихся стен для обработки

    var currentWall = remainingWalls.removeAt(0)
    var connectionFound = false
    while (remainingWalls.isNotEmpty()) {
        if(orderedWalls.isEmpty()){
            orderedWalls.add(currentWall)
        } else {
            if(!connectionFound){
                throw IllegalStateException("Incorrect data")
            }
        }
        connectionFound = false
        for (i in remainingWalls.indices) {
            val nextWall = remainingWalls[i]
            val currentEndCorner = corners.find { it.uid == currentWall.end }?.origin
                ?: throw IllegalStateException("Cannot find wall corner")
            val nextStartCorner = corners.find { it.uid == nextWall.start }?.origin
                ?: throw IllegalStateException("Cannot find wall corner")
            val nextEndCorner = corners.find { it.uid == nextWall.end }?.origin
                ?: throw IllegalStateException("Cannot find wall corner")

            if (currentEndCorner == nextStartCorner) {
                // Конец текущей стены совпадает с началом следующей
                orderedWalls.add(nextWall)
                currentWall = nextWall
                remainingWalls.removeAt(i)
                connectionFound = true
                break
            } else if (currentEndCorner == nextEndCorner) {
                // Конец текущей стены совпадает с концом следующей — переворачиваем следующую стену
                val reversedWall = reverseWallCoordinates(nextWall, nextStartCorner, nextEndCorner)
                orderedWalls.add(reversedWall)
                currentWall = reversedWall
                remainingWalls.removeAt(i)
                connectionFound = true
                break
            }
        }
    }

    return orderedWalls
}

private fun reverseWallCoordinates(wall: ApiWall, start: ApiPoint, end: ApiPoint): ApiWall {
    return wall.copy(
        start = wall.end,
        end = wall.start,
        windows = wall.windows?.map { reverseElement(it, start, end) },
        openings = wall.openings?.map { reverseElement(it, start, end) },
        doors = wall.doors?.map { reverseElement(it, start, end) }
    )
}

// Determines if a wall segment is in clockwise order
private fun isClockwise(start: ApiPoint, end: ApiPoint): Boolean {
    return start.x * end.y - end.x * start.y >= 0
}

// Reverses window or opening coordinates
private fun reverseElement(
    element: ApiWallWindow,
    wallStart: ApiPoint,
    wallEnd: ApiPoint,
): ApiWallWindow {
    // Calculate the total wall length
    val wallLength = calculateWallLength(wallStart, wallEnd)

    // Calculate the absolute width of the element
    val elementWidth = element.width / wallLength

    // Calculate the new start position
    val newStart = 1 - (element.start + elementWidth)

    return element.copy(start = newStart)
}

private fun reverseElement(
    element: ApiWallOpening,
    wallStart: ApiPoint,
    wallEnd: ApiPoint,
): ApiWallOpening {
    // Calculate the total wall length
    val wallLength = calculateWallLength(wallStart, wallEnd)

    // Calculate the absolute width of the element
    val elementWidth = element.width / wallLength

    // Calculate the new start position
    val newStart = 1 - (element.start + elementWidth)

    return element.copy(start = newStart)
}

// Reverses door coordinates
private fun reverseElement(
    element: ApiWallDoor,
    wallStart: ApiPoint,
    wallEnd: ApiPoint,
): ApiWallDoor {
    // Calculate the total wall length
    val wallLength = calculateWallLength(wallStart, wallEnd)

    // Calculate the absolute width of the door
    val elementWidth = element.width / wallLength

    // Calculate the new start position
    val newStart = 1 - (element.start + elementWidth)

    return element.copy(
        start = newStart
    )
}

// Helper function to calculate the length of a wall
private fun calculateWallLength(start: ApiPoint, end: ApiPoint): Double {
    val dx = end.x - start.x
    val dy = end.y - start.y
    return kotlin.math.sqrt(dx * dx + dy * dy)
}