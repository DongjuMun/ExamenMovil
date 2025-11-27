package com.app.examenmovile.presentation.screens.sudoku.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Suppress("ktlint:standard:function-naming")
@Composable
fun SudokuCell(
    value: Int?,
    isClue: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(35.dp)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) Color.Yellow else Color.Gray,
                ).background(if (isClue) Color(0xFFEEEEEE) else Color.White)
                .clickable(enabled = !isClue) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (value != null) {
            Text(
                text = value.toString(),
                fontSize = 18.sp,
                color = if (isClue) Color.Black else Color.Blue,
            )
        }
    }
}
