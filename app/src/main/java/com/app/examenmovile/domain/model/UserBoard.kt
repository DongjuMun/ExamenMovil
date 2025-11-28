package com.app.examenmovile.domain.model

data class UserBoard(
    val size: Int,
    val difficulty: String,
    val user: List<List<Int?>>,
)
