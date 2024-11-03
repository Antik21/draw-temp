package com.clipnow.feature.drying_area_view.mock


// Функция для нахождения смещения координат (чтобы привести все координаты в положительную область)
fun calculateOffset(corners: List<ApiCorner>): Pair<Double, Double> {
    // Найдем минимальные x и y среди всех углов
    val minX = corners.minOf { it.origin.x }
    val minY = corners.minOf { it.origin.y }

    // Вернем смещение, которое нужно применить, чтобы все координаты стали положительными
    return Pair(-minX, -minY)
}

// Функция для применения смещения ко всем углам
fun applyOffsetToCorners(corners: List<ApiCorner>, offset: Pair<Double, Double>): List<ApiCorner> {
    return corners.map { corner ->
        val newX = corner.origin.x + offset.first
        val newY = corner.origin.y + offset.second
        ApiCorner(
            uid = corner.uid,
            origin = ApiPoint(x = newX, y = newY)
        )
    }
}

// Основная функция для обработки всех комнат и преобразования координат
fun adjustCoordinatesForCanvas(rooms: List<ApiRoom>): List<ApiRoom> {
    return rooms.map { room ->
        // Находим смещение для углов комнаты
        val offset = calculateOffset(room.data.corners)

        // Применяем смещение ко всем углам
        val adjustedCorners = applyOffsetToCorners(room.data.corners, offset)

        // Создаем новую комнату с обновленными углами
        val adjustedRoomData = room.data.copy(corners = adjustedCorners)
        room.copy(data = adjustedRoomData)
    }
}