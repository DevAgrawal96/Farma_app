package com.example.farmaapp.model.newsModels

data class Articles(
    val count: Int,
    val page: Int,
    val pages: Int,
    val results: List<Result>,
    val totalResults: Int
)