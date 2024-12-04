package com.clipnow.feature.sketch_view.renderer.view.renderer

import android.graphics.Canvas


interface ElementRenderer<T> {
    fun render(canvas: Canvas, element: T, toCanvasX: (Float) -> Float, toCanvasY: (Float) -> Float)
}