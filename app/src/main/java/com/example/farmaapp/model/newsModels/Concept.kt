package com.example.farmaapp.model.newsModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Concept(
    val label: Label,
    val location: Location,
    val score: Int,
    val type: String,
    val uri: String
) : Parcelable