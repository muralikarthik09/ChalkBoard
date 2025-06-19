package com.chalkboard.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

/**
 * Simple stroke data class with only essential properties
 */
data class DrawStroke(
    val path: Path = Path(),
    val color: Color = Color.Companion.Black,
    val strokeWidth: Float = 6f,
    val isEraser: Boolean = false
)