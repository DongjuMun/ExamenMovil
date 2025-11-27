package com.app.examenmovile.data.remote.api

import com.app.examenmovile.data.remote.dto.SudokuDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SudokuApi {
    @GET("v1/sudokugenerate")
    suspend fun getSudoku(
        @Header("X-Api-Key") apiKey: String,
        @Query("width") width: Int? = null,
        @Query("height") height: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("seed") seed: Long? = null,
    ): SudokuDto
}
