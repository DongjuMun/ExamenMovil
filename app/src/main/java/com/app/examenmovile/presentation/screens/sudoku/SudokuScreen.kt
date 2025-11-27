package com.app.examenmovile.presentation.screens.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.presentation.screens.sudoku.components.SudokuCell
import com.app.examenmovile.presentation.screens.sudoku.components.SudokuNumberButton

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen(
    size: Int,
    difficulty: String,
    onBackClick: () -> Unit,
    viewModel: SudokuViewModel = hiltViewModel(),
) {
    LaunchedEffect(size, difficulty) {
        viewModel.getSudoku(size, difficulty)
    }
    val uiState by viewModel.uiState.collectAsState()
    var selectedRow by remember { mutableStateOf(-1) }
    var selectedCol by remember { mutableStateOf(-1) }

    // Editable board state
    val board = remember { mutableStateListOf<List<Int?>>() }
    if (board.isEmpty()) board.addAll(uiState.sudoku.puzzle)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sudoku $size×$size - $difficulty") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Sudoku board
            LazyVerticalGrid(
                columns = GridCells.Fixed(size),
                modifier =
                    Modifier
                        .size(300.dp)
                        .background(Color.Black),
            ) {
                items(size * size) { index ->
                    val row = index / size
                    val col = index % size
                    val value = board[row][col]

                    SudokuCell(
                        value = value,
                        isClue = value != null,
                        isSelected = (row == selectedRow && col == selectedCol),
                        onClick = {
                            if (value == null) {
                                selectedRow = row
                                selectedCol = col
                            }
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Number pad
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                val maxNumber = size
                for (n in 1..maxNumber) {
                    SudokuNumberButton(
                        number = n,
                        onClick = {
                            if (selectedRow != -1 && selectedCol != -1) {
                                val newRow = board[selectedRow].toMutableList()
                                newRow[selectedCol] = n
                                board[selectedRow] = newRow
                            }
                        },
                    )
                }
            }
        }
    }
}
