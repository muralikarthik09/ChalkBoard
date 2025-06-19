package com.chalkboard.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.chalkboard.data.models.DrawStroke

@Composable
fun ChalkBoardScreen() {
    // List to store all drawn strokes (automatically triggers recomposition when changed)
    val paths = remember { mutableStateListOf<DrawStroke>() }

    // Toggle between eraser and pencil modes
    var isEraser by remember { mutableStateOf(false) }

    // Current path being drawn (stateful to trigger immediate updates)
    var currentPath by remember { mutableStateOf(Path()) }

    // Reference to the current stroke being drawn
    var currentStroke by remember { mutableStateOf<DrawStroke?>(null) }

    // Main screen layout
    Column(modifier = Modifier.fillMaxSize()) {
        // Drawing canvas area
        Canvas(
            modifier = Modifier
                .weight(1f)  // Takes all available space
                .fillMaxWidth()
                .background(Color.White)  // White background for canvas
                .pointerInput(Unit) {  // Touch input handling
                    detectDragGestures(
                        // When user starts drawing (finger touches screen)
                        onDragStart = { offset ->
                            // Create new path starting at touch position
                            currentPath = Path().apply {
                                moveTo(offset.x, offset.y)
                            }

                            // Create new stroke with current settings
                            currentStroke = DrawStroke(
                                path = currentPath,
                                color = if (isEraser) Color.White else Color.Black,
                                strokeWidth = if (isEraser) 40f else 6f
                            )

                            // Add to list of paths
                            paths.add(currentStroke!!)
                        },

                        // When user moves finger (drawing)
                        onDrag = { change, _ ->
                            // Create new path that copies existing path and adds new line segment
                            val newPath = Path().apply {
                                addPath(currentPath)
                                lineTo(change.position.x, change.position.y)
                            }

                            // Update current path (triggers recomposition)
                            currentPath = newPath

                            // Update the last path in the list with new path data
                            paths[paths.lastIndex] = DrawStroke(
                                path = currentPath,
                                color = if (isEraser) Color.White else Color.Black,
                                strokeWidth = if (isEraser) 40f else 6f
                            )
                        },

                        // When user lifts finger (ends drawing)
                        onDragEnd = {
                            currentStroke = null  // Clear current stroke reference
                        }
                    )
                }
        ) {
            // Draw all saved paths on the canvas
            paths.forEach { stroke ->
                drawPath(
                    path = stroke.path,
                    color = stroke.color,
                    style = Stroke(width = stroke.strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Toolbar at bottom with control buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Pencil mode button
            Button(onClick = { isEraser = false }) {
                Text("Pencil")
            }

            // Eraser mode button
            Button(onClick = { isEraser = true }) {
                Text("Eraser")
            }

            // Clear canvas button
            Button(onClick = { paths.clear() }) {
                Text("Clear")
            }
        }
    }
}