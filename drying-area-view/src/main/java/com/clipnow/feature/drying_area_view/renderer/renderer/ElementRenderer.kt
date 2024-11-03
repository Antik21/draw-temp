package com.clipnow.feature.drying_area_view.renderer.renderer

import android.graphics.Canvas


interface ElementRenderer<T> {
    fun render(canvas: Canvas, element: T, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float)
}