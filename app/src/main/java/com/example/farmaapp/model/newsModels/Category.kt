package com.example.farmaapp.model.newsModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val label: String,
    val uri: String,
    val wgt: Int
) : Parcelable