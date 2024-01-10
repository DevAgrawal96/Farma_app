package com.example.farmaapp.model.newsModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Author(
    val isAgency: Boolean,
    val name: String,
    val type: String,
    val uri: String
) : Parcelable