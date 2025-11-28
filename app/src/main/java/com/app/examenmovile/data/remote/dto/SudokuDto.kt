@file:Suppress("ktlint:standard:filename")

package com.app.examenmovile.data.remote.dto

data class SudokuDto(
    val size: Int,
    val difficulty: String,
    val puzzle: List<List<Int?>>,
)
