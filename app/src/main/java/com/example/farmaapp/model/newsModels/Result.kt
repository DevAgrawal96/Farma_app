package com.example.farmaapp.model.newsModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val authors: List<Author>,
    val body: String,
    val categories: List<Category>,
    val concepts: List<Concept>,
    val dataType: String,
    val date: String,
    val dateTime: String,
    val dateTimePub: String,
    val eventUri: String,
    val image: String,
    val isDuplicate: Boolean,
    val lang: String,
    val relevance: Int,
    val sentiment: Double,
    val shares: Shares,
    val sim: Double,
    val source: Source,
    val time: String,
    val title: String,
    val uri: String,
    val url: String,
    val wgt: Int
) : Parcelable