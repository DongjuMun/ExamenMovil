package com.app.examenmovile.domain.model

data class Solution(
    val size: Int,
    val difficulty: String,
    val status: String,
    val solution: List<List<Int>>? = null,
)
