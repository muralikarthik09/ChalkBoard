package com.chalkboard.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.chalkboard.data.models.DrawStroke

@Composable
fun ChalkBoardScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val paths = remember { mutableStateListOf<DrawStroke>() }
    var isEraser by remember { mutableStateOf(false) }
    var currentPath by remember { mutableStateOf(Path()) }
    var currentStroke by remember { mutableStateOf<DrawStroke?>(null) }

    if (isLandscape) {
        Row(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                                currentStroke = DrawStroke(
                                    path = currentPath,
                                    color = if (isEraser) Color.White else Color.Black,
                                    strokeWidth = if (isEraser) 40f else 6f
                                )
                                paths.add(currentStroke!!)
                            },
                            onDrag = { change, _ ->
                                val newPath = Path().apply {
                                    addPath(currentPath)
                                    lineTo(change.position.x, change.position.y)
                                }
                                currentPath = newPath
                                paths[paths.lastIndex] = DrawStroke(
                                    path = currentPath,
                                    color = if (isEraser) Color.White else Color.Black,
                                    strokeWidth = if (isEraser) 40f else 6f
                                )
                            },
                            onDragEnd = {
                                currentStroke = null
                            }
                        )
                    }
            ) {
                paths.forEach { stroke ->
                    drawPath(
                        path = stroke.path,
                        color = stroke.color,
                        style = Stroke(width = stroke.strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            DrawingLandScapeControls(
                isEraserActive = isEraser,
                onPencilClick = { isEraser = false },
                onEraserClick = { isEraser = true },
                onClearClick = { paths.clear() },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                                currentStroke = DrawStroke(
                                    path = currentPath,
                                    color = if (isEraser) Color.White else Color.Black,
                                    strokeWidth = if (isEraser) 40f else 6f
                                )
                                paths.add(currentStroke!!)
                            },
                            onDrag = { change, _ ->
                                val newPath = Path().apply {
                                    addPath(currentPath)
                                    lineTo(change.position.x, change.position.y)
                                }
                                currentPath = newPath
                                paths[paths.lastIndex] = DrawStroke(
                                    path = currentPath,
                                    color = if (isEraser) Color.White else Color.Black,
                                    strokeWidth = if (isEraser) 40f else 6f
                                )
                            },
                            onDragEnd = {
                                currentStroke = null
                            }
                        )
                    }
            ) {
                paths.forEach { stroke ->
                    drawPath(
                        path = stroke.path,
                        color = stroke.color,
                        style = Stroke(width = stroke.strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            DrawingPortraitControls(
                isEraserActive = isEraser,
                onPencilClick = { isEraser = false },
                onEraserClick = { isEraser = true },
                onClearClick = { paths.clear() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}
