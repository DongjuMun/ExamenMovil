@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.examenmovile.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.examenmovile.domain.model.Sudoku

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onClick: (Int, String) -> Unit) {
    var expanded1 by remember { mutableStateOf(false) }
    var selected1 by remember { mutableStateOf("Selecciona tamaño") }

    var expanded2 by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf("Selecciona dificultad") }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Elige el tipo de sudoku",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // ---- TAMAÑO ----
            Box {
                OutlinedButton(
                    onClick = { expanded1 = true },
                ) {
                    Text(selected1)
                }

                DropdownMenu(
                    expanded = expanded1,
                    onDismissRequest = { expanded1 = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("4x4") },
                        onClick = {
                            selected1 = "4x4"
                            expanded1 = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("9x9") },
                        onClick = {
                            selected1 = "9x9"
                            expanded1 = false
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---- DIFICULTAD ----
            Box {
                OutlinedButton(
                    onClick = { expanded2 = true },
                ) {
                    Text(selected2)
                }

                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("easy") },
                        onClick = {
                            selected2 = "easy"
                            expanded2 = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("medium") },
                        onClick = {
                            selected2 = "medium"
                            expanded2 = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("hard") },
                        onClick = {
                            selected2 = "hard"
                            expanded2 = false
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    Log.d("SendData", "$selected1 and $selected2")
                    if (selected1 == "4x4") {
                        onClick(2, selected2)
                    }
                    if (selected1 == "9x9") {
                        onClick(3, selected2)
                    }
                },
            ) {
                Text("Empezar")
            }
        }
    }
}
