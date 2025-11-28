package com.app.examenmovile.data.remote.dto

data class SolutionDto(
    val size: Int,
    val difficulty: String,
    val status: String,
    val solution: List<List<Int>>?,
)
