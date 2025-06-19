package com.chalkboard.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chalkboard.R

@Composable
fun DrawingPortraitControls(
    isEraserActive: Boolean,
    onPencilClick: () -> Unit,
    onEraserClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DrawingButtons(isEraserActive, onPencilClick, onEraserClick, onClearClick)
    }
}

@Composable
fun DrawingLandScapeControls(
    isEraserActive: Boolean,
    onPencilClick: () -> Unit,
    onEraserClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        DrawingButtons(isEraserActive, onPencilClick, onEraserClick, onClearClick)
    }
}

@Composable
private fun DrawingButtons(
    isEraserActive: Boolean,
    onPencilClick: () -> Unit,
    onEraserClick: () -> Unit,
    onClearClick: () -> Unit
) {
    ToolIconButton(
        iconResId = R.drawable.ic_pencil,
        isSelected = !isEraserActive,
        onClick = onPencilClick,
        contentDescription = "Pencil"
    )
    ToolIconButton(
        iconResId = R.drawable.ic_eraser,
        isSelected = isEraserActive,
        onClick = onEraserClick,
        contentDescription = "Eraser"
    )
    ToolIconButton(
        iconResId = R.drawable.ic_clearall,
        isSelected = false,
        onClick = onClearClick,
        contentDescription = "Clear canvas"
    )
}

@Composable
private fun ToolIconButton(
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp)
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(24.dp)
        )
    }
}
